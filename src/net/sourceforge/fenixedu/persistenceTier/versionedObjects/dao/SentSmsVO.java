package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.domain.sms.SentSms;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.sms.IPersistentSentSms;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 *  
 */
public class SentSmsVO extends VersionedObjectsBase implements IPersistentSentSms {

    public List readByPerson(final Integer personID, Integer interval) throws ExcepcaoPersistencia {

		Collection<SentSms> sentSmsList = readAll(SentSms.class);
		
		CollectionUtils.filter(sentSmsList, new Predicate() {
		
			public boolean evaluate(Object arg0) {
				SentSms sms = (SentSms) arg0;
				if (sms.getPerson().getIdInternal().equals(personID)){
					return true;
				}
				return false;
			}
		
		});
		
		Collections.sort((List<SentSms>)sentSmsList, new Comparator() {
		
			public int compare(Object o1, Object o2) {
				SentSms sms1 = (SentSms) o1;
				SentSms sms2 = (SentSms) o2;
				return sms1.getSendDate().compareTo(sms2.getSendDate());
			}		
		});
		
		List<SentSms> result = new ArrayList(interval);
		for (SentSms sms : sentSmsList) {
			if (interval > 0) {
				result.add(sms);
				interval--;
			}
		}
		
		return result;
    }

    public Integer countByPersonAndDatePeriod(final Integer personId, Date startDate, Date endDate) {

		Collection<SentSms> sentSmsList = readAll(SentSms.class);
		
		CollectionUtils.filter(sentSmsList, new Predicate() {
		
			public boolean evaluate(Object arg0) {
				SentSms sms = (SentSms) arg0;
				if (sms.getPerson().getIdInternal().equals(personId)){
					return true;
				}
				return false;
			}		
		});
		
		Integer result = 0;
		
		for (SentSms sms : sentSmsList) {
			if ( sms.getSendDate().before(endDate) && sms.getSendDate().after(startDate)) {
				result++;
			}
		}
		return result;
    }

}