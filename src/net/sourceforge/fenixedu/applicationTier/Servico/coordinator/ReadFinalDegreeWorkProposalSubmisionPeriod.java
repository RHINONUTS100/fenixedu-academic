/*
 * Created on 2004/03/10
 */
package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork.InfoScheduleing;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Scheduleing;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentFinalDegreeWork;

/**
 * @author Luis Cruz
 */
public class ReadFinalDegreeWorkProposalSubmisionPeriod extends Service {

    public InfoScheduleing run(Integer executionDegreeOID) throws FenixServiceException, ExcepcaoPersistencia {

        InfoScheduleing infoScheduleing = null;

        if (executionDegreeOID != null) {

            IPersistentFinalDegreeWork persistentFinalDegreeWork = persistentSupport
                    .getIPersistentFinalDegreeWork();

            ExecutionDegree cursoExecucao = (ExecutionDegree) persistentFinalDegreeWork.readByOID(
                    ExecutionDegree.class, executionDegreeOID);

            if (cursoExecucao != null) {
                Scheduleing scheduleing = persistentFinalDegreeWork
                        .readFinalDegreeWorkScheduleing(executionDegreeOID);

                if (scheduleing != null) {
                    infoScheduleing = new InfoScheduleing();
                    infoScheduleing.setIdInternal(scheduleing.getIdInternal());
                    infoScheduleing.setStartOfProposalPeriod(scheduleing.getStartOfProposalPeriod());
                    infoScheduleing.setEndOfProposalPeriod(scheduleing.getEndOfProposalPeriod());
                    infoScheduleing.setStartOfCandidacyPeriod(scheduleing.getStartOfCandidacyPeriod());
                    infoScheduleing.setEndOfCandidacyPeriod(scheduleing.getEndOfCandidacyPeriod());
                    infoScheduleing.setMinimumNumberOfCompletedCourses(scheduleing
                            .getMinimumNumberOfCompletedCourses());
                    infoScheduleing.setMinimumNumberOfStudents(scheduleing.getMinimumNumberOfStudents());
                    infoScheduleing.setMaximumNumberOfStudents(scheduleing.getMaximumNumberOfStudents());
                    infoScheduleing.setMaximumNumberOfProposalCandidaciesPerGroup(scheduleing
                            .getMaximumNumberOfProposalCandidaciesPerGroup());
                }
            }
        }

        return infoScheduleing;
    }

}