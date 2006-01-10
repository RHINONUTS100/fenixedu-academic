/*
 * Created on 28/Ago/2003, 7:57:10
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentWithInfoPerson;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 * Created at 28/Ago/2003, 7:57:10
 * 
 */
public class ReadStudentById implements IService {

	public Object run(Integer id) throws FenixServiceException, ExcepcaoPersistencia {
		InfoStudent infoStudent = null;

		ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
		Student student = (Student) sp.getIPersistentStudent().readByOID(Student.class, id);
		if (student != null) {
			infoStudent = InfoStudentWithInfoPerson.newInfoFromDomain(student);
		}

		return infoStudent;
	}
}