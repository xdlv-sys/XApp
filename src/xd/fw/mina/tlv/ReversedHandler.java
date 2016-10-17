package xd.fw.mina.tlv;

import org.apache.commons.lang3.StringUtils;
import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Value;

import java.util.*;

public class ReversedHandler extends TLVHandler implements IMinaConst, ProxyListener{
    @Value("${mina_timeout}")
    int minaTimeout;

    final static List<String> discardRequests = new LinkedList<>();

    static Map<String, IoSession> sessionMap = new HashMap<>();

    static List<ProxyListener> proxyListeners = new ArrayList<>();

    protected static void addProxyListeners(ProxyListener listener) {
        if (!proxyListeners.contains(listener)) {
            proxyListeners.add(listener);
        }
    }

    public ReversedHandler(){
        addProxyListeners(this);
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        TLVMessage msg = (TLVMessage) message;
        int code = (int) msg.getValue();
        if (code == REGISTRY) {
            String id = (String) msg.getNextValue(0);
            if ("000".equals(id)){
                return;
            }
            synchronized (sessionMap) {
                IoSession proxySession = sessionMap.get(id);
                if (session != proxySession) {
                    if (proxySession != null){
                        proxySession.closeNow();
                        logger.info("drop old session:{} from {}", id, proxySession.getRemoteAddress());
                    }
                    sessionMap.put(id, session);
                    for (ProxyListener listener : proxyListeners) {
                        listener.proxyCreated(id, session);
                    }
                }
            }
            session.setAttribute(ID_KEY, id);
            handlerRegistry(msg, session);
            return;
        }
        logger.info("receive:" + message);

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


    protected TLVMessage createRequest(Object ... objs){
        TLVMessage message = new TLVMessage(objs[0]);
        // add timestamp after code
        TLVMessage next = message.setNext(String.valueOf(System.currentTimeMillis()));
        int i = 0;
        while (objs.length > ++i){
            next = next.setNext(objs[i]);
        }
        return message;
    }

    private void removeSession(IoSession session) {
        String id = (String) session.getAttribute(ID_KEY);
        if (StringUtils.isNotBlank(id)) {
            synchronized (sessionMap) {
                if (sessionMap.remove(id) != null) {
                    for (ProxyListener listener : proxyListeners) {
                        listener.proxyRemoved(id, session);
                    }
                }
            }
        }
    }

    private IoSession getSession(String parkId) {
        synchronized (sessionMap) {
            return sessionMap.get(parkId);
        }
    }

    protected TLVMessage request(String parkId, TLVMessage message) {
        IoSession session = getSession(parkId);
        if (session == null) {
            logger.info("there is no park session:" + parkId);
            return null;
        }
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

    @Override
    public void proxyCreated(String parkId, IoSession session) {
        logger.info("proxy create:{} from {}", parkId, session.getRemoteAddress());
    }

    @Override
    public void proxyRemoved(String parkId,IoSession session) {
        logger.info("proxy remove:{}", parkId);
    }
}