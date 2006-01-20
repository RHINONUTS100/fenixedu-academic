/*
 * Created on Dec 11, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.degree.execution;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionYear;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

/**
 * @author jpvl
 */
public class ReadExecutionDegreesByExecutionYearAndDegreeType extends Service {

    public List run(InfoExecutionYear infoExecutionYear, DegreeType degreeType)
            throws ExcepcaoPersistencia {

        List infoExecutionDegreeList = null;

        IPersistentExecutionDegree executionDegreeDAO = persistentSupport.getIPersistentExecutionDegree();

		String executionYear = null;
        if (infoExecutionYear == null) {
            IPersistentExecutionYear persistentExecutionYear = persistentSupport.getIPersistentExecutionYear();
            executionYear = persistentExecutionYear.readCurrentExecutionYear().getYear();
        } else {
            executionYear = infoExecutionYear.getYear();
        }

        List executionDegrees = null;

        if (degreeType == null) {
            executionDegrees = executionDegreeDAO.readByExecutionYear(executionYear);
        } else {
            executionDegrees = executionDegreeDAO.readByExecutionYearAndDegreeType(executionYear,
                    degreeType);
        }

        infoExecutionDegreeList = (ArrayList) CollectionUtils.collect(executionDegrees,
                new Transformer() {

                    public Object transform(Object input) {
                        ExecutionDegree executionDegree = (ExecutionDegree) input;
                        InfoExecutionDegree infoExecutionDegree = InfoExecutionDegree
                                .newInfoFromDomain(executionDegree);

                        InfoExecutionYear infoExecutionYear = InfoExecutionYear
                                .newInfoFromDomain(executionDegree.getExecutionYear());
                        infoExecutionDegree.setInfoExecutionYear(infoExecutionYear);

                        InfoDegreeCurricularPlan infoDegreeCurricularPlan = InfoDegreeCurricularPlan
                                .newInfoFromDomain(executionDegree.getDegreeCurricularPlan());
                        infoExecutionDegree.setInfoDegreeCurricularPlan(infoDegreeCurricularPlan);

                        InfoDegree infoDegree = InfoDegree.newInfoFromDomain(executionDegree
                                .getDegreeCurricularPlan().getDegree());
                        infoDegreeCurricularPlan.setInfoDegree(infoDegree);

                        return infoExecutionDegree;
                    }
                });

        return infoExecutionDegreeList;
    }
}