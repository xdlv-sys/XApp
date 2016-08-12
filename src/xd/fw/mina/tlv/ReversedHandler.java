package xd.fw.mina.tlv;

import org.apache.commons.lang3.StringUtils;
import org.apache.mina.core.session.IoSession;

import java.util.*;

public class ReversedHandler extends TLVHandler implements IMinaConst{

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
        if (logger.isDebugEnabled()) {
            logger.debug("receive:" + message);
        }
        TLVMessage msg = (TLVMessage) message;
        byte code = (byte) msg.getValue();
        if (code == REGISTRY) {
            String id = (String) msg.getNextValue(0);
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

        String messageId = (String)msg.getNextValue(1);

        boolean discard;
        synchronized (discardRequests) {
            discard = discardRequests.remove(messageId);
        }
        if (discard) {
            logger.debug("discard message for timeout: " + messageId);
        } else {
            session.setAttribute(messageId, msg.getNext());
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
                    logger.info("remove proxy session:" + id);
                    for (ProxyListener listener : proxyListeners) {
                        listener.proxyRemoved(id);
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
            return null;
        }
        // timestamp is just behind code
        String messageId = (String)message.getNextValue(1);
        session.write(message);

        TLVMessage ret;
        int count = 0;
        while ((ret = (TLVMessage) session.removeAttribute(messageId)) == null) {
            if (count++ > 200) {
                break;
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                logger.error("", e);
            }
        }
        if (ret == null) {
            synchronized (discardRequests) {
                discardRequests.add(messageId);
            }

            logger.debug("add discard message:" + messageId);
            return null;
        }
        /*remove code adn timestamp*/
        return (byte) ret.getValue() == NULL_MSG ? null : ret.getNext(1);
    }
}
