package xd.dl.service;

import org.springframework.stereotype.Service;
import xd.fw.service.impl.HibernateServiceImpl;


@Service
public class ParkServiceImpl extends HibernateServiceImpl implements ParkService {

    /*@Override
    public List<ParkInfo> getAllParkInfo(int start, int limit) {
        return htpl.execute(new HibernateCallback<List<ParkInfo>>() {
            @Override
            public List<ParkInfo> doInHibernate(Session session) throws HibernateException {
                Query query = session.createQuery("select new ParkInfo( parkId,  parkName, appId" +
                        ", limitPay,  partnerId,freeCount, proxyState) from ParkInfo order by parkId");
                query.setFirstResult(start);
                query.setMaxResults(limit);
                return query.list();
            }
        });
    }

    @Override
    public List<PayOrder> getAllPayOrder(int start, int limit) {
        return htpl.execute(new HibernateCallback<List<PayOrder>>() {
            @Override
            public List<PayOrder> doInHibernate(Session session) throws HibernateException {
                Query query = session.createQuery("from PayOrder order by timeStamp");
                query.setFirstResult(start);
                query.setMaxResults(limit);
                return query.list();
            }
        });
    }*/
}
