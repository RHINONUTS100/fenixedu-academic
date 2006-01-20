/*
 * Created on 13/Abr/2005 - 16:20:23
 * 
 */

package net.sourceforge.fenixedu.applicationTier.Servico.gep.inquiries;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.InfoInquiriesRegistry;
import net.sourceforge.fenixedu.domain.inquiries.InquiriesRegistry;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.inquiries.IPersistentInquiriesRegistry;

/**
 * @author Jo�o Fialho & Rita Ferreira
 * 
 */
public class ReadInquiriesRegistriesByStudent extends Service {

    public List<InfoInquiriesRegistry> run(InfoStudent infoStudent) throws FenixServiceException,
            ExcepcaoPersistencia, NoSuchMethodException, InvocationTargetException,
            NoSuchMethodException, IllegalAccessException {

        if (infoStudent == null) {
            throw new FenixServiceException("nullInfoStudent");
        }

        IPersistentInquiriesRegistry inquiriesRegistryDAO = persistentSupport.getIPersistentInquiriesRegistry();
        List<InquiriesRegistry> inquiriesRegistries = inquiriesRegistryDAO.readByStudentId(infoStudent
                .getIdInternal());

        List<InfoInquiriesRegistry> infoInquiriesRegistries = new ArrayList<InfoInquiriesRegistry>(
                inquiriesRegistries.size());

        for (InquiriesRegistry inquiriesRegistry : inquiriesRegistries) {
            infoInquiriesRegistries.add(InfoInquiriesRegistry.newInfoFromDomain(inquiriesRegistry));
        }

        return infoInquiriesRegistries;
    }

}
