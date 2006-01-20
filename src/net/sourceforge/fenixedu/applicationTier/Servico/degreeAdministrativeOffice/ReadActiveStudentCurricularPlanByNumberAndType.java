/**
 * Jul 27, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.degreeAdministrativeOffice;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlanWithEquivalencesAndInfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentCurricularPlan;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class ReadActiveStudentCurricularPlanByNumberAndType extends Service {

    public InfoStudentCurricularPlanWithEquivalencesAndInfoDegreeCurricularPlan run(
            Integer studentNumber, DegreeType degreeType) throws ExcepcaoPersistencia {
        IPersistentStudentCurricularPlan persistentStudentCurricularPlan = persistentSupport
                .getIStudentCurricularPlanPersistente();

        StudentCurricularPlan scp = persistentStudentCurricularPlan
                .readActiveByStudentNumberAndDegreeType(studentNumber, degreeType);

        InfoStudentCurricularPlanWithEquivalencesAndInfoDegreeCurricularPlan infoSCP = 
            InfoStudentCurricularPlanWithEquivalencesAndInfoDegreeCurricularPlan.newInfoFromDomain(scp);

        List infoCurricularCourses = (List) CollectionUtils.collect(scp.getDegreeCurricularPlan()
                .getCurricularCourses(), new Transformer() {

            public Object transform(Object arg0) {
                CurricularCourse curricularCourse = (CurricularCourse) arg0;
                return InfoCurricularCourse.newInfoFromDomain(curricularCourse);
            }
        });

        infoSCP.getInfoDegreeCurricularPlan().setCurricularCourses(infoCurricularCourses);
        return infoSCP;
    }

}
