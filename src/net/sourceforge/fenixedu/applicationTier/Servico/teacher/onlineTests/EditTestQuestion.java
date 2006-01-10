/*
 * Created on 6/Ago/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.onlineTests.TestQuestion;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.tests.CorrectionFormula;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Susana Fernandes
 */
public class EditTestQuestion implements IService {

    public void run(Integer executionCourseId, Integer testQuestionId, Integer testQuestionOrder, Double testQuestionValue, CorrectionFormula formula)
            throws ExcepcaoPersistencia, FenixServiceException {
        ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        TestQuestion testQuestion = (TestQuestion) persistentSuport.getIPersistentTestQuestion().readByOID(TestQuestion.class, testQuestionId);
        if (testQuestion == null) {
            throw new InvalidArgumentsServiceException();
        }
        if (testQuestionOrder == -1) {
            testQuestionOrder = testQuestion.getTest().getTestQuestions().size();
        } else if (testQuestionOrder == -2) {
            testQuestionOrder = testQuestion.getTestQuestionOrder();
        }
        if (testQuestionOrder.compareTo(testQuestion.getTestQuestionOrder()) < 0) {
            testQuestionOrder++;
        }

        testQuestion.editTestQuestion(testQuestionOrder, testQuestionValue, formula);
    }
}