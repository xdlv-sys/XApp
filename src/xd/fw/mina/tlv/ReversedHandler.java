package xd.fw.mina.tlv;

import org.apache.commons.lang3.StringUtils;
import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Value;

import java.util.*;

public class ReversedHandler extends TLVHandler implements IMinaConst{
    @Value("${mina_timeout}")
    int minaTimeout;

    static List<String> discardRequests = new LinkedList<>();

    static Map<String, IoSession> sessionMap = new HashMap<>();

    interface ProxyListener {
        void proxyCreated(String parkId);

        void proxyRemoved(String parkId);
    }

    static List<ProxyListener> proxyListeners = new ArrayList<>();

    static void addProxyListeners(ProxyListener listener) {
        if (!proxyListeners.contains(listener)) {
            proxyListeners.add(listener);
        }
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        logger.info("receive:" + message);
        TLVMessage msg = (TLVMessage) message;
        int code = (int) msg.getValue();
        if (code == REGISTRY) {
            String ID = (String) msg.getNextValue(0);
            synchronized (sessionMap) {
                IoSession proxySession = sessionMap.get(id);
                if (session != proxySession) {
                    sessionMap.put(id, session);
                    for (ProxyListener listener : proxyListeners) {
                        listener.proxyCreated(id);
                    }
                }
            }
            session.setAttribute(ID_KEY, id);
            handlerRegistry(msg, session);
            return;
        }

        // message is handled already
        if (handlerMessage(msg,session)){
            return;
        }

        String messageId = (String)msg.getNextValue(0);

        boolean discard;
        synchronized (discardRequests) {
            discard = discardRequests.remove(messageId);
        }
        if (discard) {
            logger.debug("discard message for timeout: " + messageId);
        } else {
            session.setAttribute(messageId, msg);
        }
    }

    protected boolean handlerMessage(TLVMessage msg, IoSession session){
        return false;
    }

    protected void handlerRegistry(TLVMessage msg, IoSession session) {
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        super.sessionClosed(session);
        removeSession(session);
    }


    protected TLVMessage createRequest(Object ... args){
        TLVMessage message = new TLVMessage(args[0]);
        // add timestamp after code
        TLVMessage next = message.setNext(generateId());
        int i = 0;
        while (args.length > ++i){
            next = next.setNext(args[i]);
        }
        return message;
    }

    private void removeSession(IoSession session) {
        String id = (String) session.getAttribute(ID_KEY);
        if (StringUtils.isNotBlank(id)) {
            synchronized (sessionMap) {
                if (sessionMap.remove(id) != null) {
                    logger.info("remove proxy session:" + id);
                    for (ProxyListener listener : proxyListeners) {
                        listener.proxyRemoved(id);
                    }
                }
            }
        }
    }

    protected String generateId(){
        return String.valueOf(System.currentTimeMillis());
    }

    private IoSession getSession(String id) {
        synchronized (sessionMap) {
            return sessionMap.get(id);
        }
    }

    protected List<TLVMessage> notifyAllId(TLVMessage message){
        List<TLVMessage> messages = new ArrayList<>();
        Collection<IoSession> sessions = new HashSet<>();
        synchronized (sessionMap) {
            sessions.addAll(sessionMap.values());
        }
        TLVMessage ret;
        for (IoSession session : sessions){
            ret = doSend(session, message);
            if (ret == null){
                logger.warn("fail to notify {}" , session.getAttribute(ID_KEY));
            } else {
                messages.add(ret);
            }
            //reset message id
            message.getNext(0).setValue(generateId());
        }

        return messages;
    }

    protected TLVMessage request(String id, TLVMessage message) {
        IoSession session = getSession(id);
        if (session == null) {
            logger.info("there is no park session:" + id);
            return null;
        }
        return doSend(session, message);
    }

    private TLVMessage doSend(IoSession session, TLVMessage message){

        // timestamp is just behind code
        String messageId = (String)message.getNextValue(0);
        session.write(message).awaitUninterruptibly();

        TLVMessage ret;
        int count = 0;
        while ((ret = (TLVMessage) session.removeAttribute(messageId)) == null) {
            if (count++ > minaTimeout) {
                break;
            }
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                logger.error("", e);
            }
        }
        if (ret == null) {
            synchronized (discardRequests) {
                discardRequests.add(messageId);
            }

            logger.info("add discard message:" + messageId);
            return null;
        }
        /*remove code adn timestamp*/
        ret = ret.getNext(1);
        if (ret.getValue() instanceof Integer
                && (int) ret.getValue() == NULL_MSG){
            return null;
        }
        return ret;
    }
}
