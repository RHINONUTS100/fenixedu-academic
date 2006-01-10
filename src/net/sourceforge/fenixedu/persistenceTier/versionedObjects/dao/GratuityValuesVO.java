/*
 * Created on 9/Jan/2004
 *
 */
package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.GratuityValues;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentGratuityValues;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

/**
 * @author T�nia Pous�o
 * 
 */
public class GratuityValuesVO extends VersionedObjectsBase implements IPersistentGratuityValues {

    public GratuityValues readGratuityValuesByExecutionDegree(Integer executionDegreeID)
            throws ExcepcaoPersistencia {

        final ExecutionDegree executionDegree = (ExecutionDegree) readByOID(ExecutionDegree.class,
                executionDegreeID);
        return executionDegree.getGratuityValues();

    }

}