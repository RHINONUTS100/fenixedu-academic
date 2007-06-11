package net.sourceforge.fenixedu.domain.organizationalStructure;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.UnitSite;
import net.sourceforge.fenixedu.domain.accessControl.DepartmentEmployeesByExecutionYearGroup;
import net.sourceforge.fenixedu.domain.accessControl.DepartmentStudentsByExecutionYearGroup;
import net.sourceforge.fenixedu.domain.accessControl.DepartmentTeachersByExecutionYearGroup;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.injectionCode.IGroup;

import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;

public class DepartmentUnit extends DepartmentUnit_Base {
    
    private DepartmentUnit() {
	super();
	super.setType(PartyTypeEnum.DEPARTMENT);
    }
       
    public static DepartmentUnit createNewInternalDepartmentUnit(String departmentName, Integer costCenterCode, String departmentAcronym,
	    YearMonthDay beginDate, YearMonthDay endDate, Unit parentUnit, AccountabilityType accountabilityType,
	    String webAddress, Department department, UnitClassification classification, Boolean canBeResponsibleOfSpaces) {
			
	DepartmentUnit departmentUnit = new DepartmentUnit();
	departmentUnit.init(departmentName, costCenterCode, departmentAcronym, beginDate, endDate, webAddress, classification, canBeResponsibleOfSpaces);
	departmentUnit.setDepartment(department);
	departmentUnit.addParentUnit(parentUnit, accountabilityType);		
	
	checkIfAlreadyExistsOneDepartmentUnitWithSameAcronymAndName(departmentUnit);
	
	return departmentUnit;
    }    
    
    public static DepartmentUnit createNewOfficialExternalDepartmentUnit(final String departmentName, final String departmentAcronym, final Unit parentUnit) {							
	
	final DepartmentUnit departmentUnit = new DepartmentUnit();
	departmentUnit.init(departmentName, null, departmentAcronym, new YearMonthDay(), null, null, null, null);
	if(parentUnit.isCountryUnit()) {
	    departmentUnit.addParentUnit(parentUnit, AccountabilityType.readAccountabilityTypeByType(AccountabilityTypeEnum.GEOGRAPHIC));
	} else {
	    departmentUnit.addParentUnit(parentUnit, AccountabilityType.readAccountabilityTypeByType(AccountabilityTypeEnum.ORGANIZATIONAL_STRUCTURE));
	}
	
	checkIfAlreadyExistsOneDepartmentUnitWithSameAcronymAndName(departmentUnit);
	return departmentUnit;
    }
    
    @Override
    public void edit(String name, String acronym) {
        super.edit(name, acronym);
        checkIfAlreadyExistsOneDepartmentUnitWithSameAcronymAndName(this);
    }
                  
    @Override
    public void edit(String unitName, Integer unitCostCenter, String acronym, YearMonthDay beginDate,
            YearMonthDay endDate, String webAddress, UnitClassification classification, Department department, 
            Degree degree, AdministrativeOffice administrativeOffice, Boolean canBeResponsibleOfSpaces) {
    	
	super.edit(unitName, unitCostCenter, acronym, beginDate, endDate, webAddress, classification, department, degree, administrativeOffice, canBeResponsibleOfSpaces);
	if(isInternal()) {
	    setDepartment(department);
	}
	
	checkIfAlreadyExistsOneDepartmentUnitWithSameAcronymAndName(this);
    }
    
    public List<CompetenceCourse> getDepartmentUnitCompetenceCourses(CurricularStage curricularStage) {
	List<CompetenceCourse> result = new ArrayList<CompetenceCourse>();
	for (ScientificAreaUnit scientificAreaUnit : getScientificAreaUnits()) {
	    for (CompetenceCourseGroupUnit competenceCourseGroupUnit : scientificAreaUnit.getCompetenceCourseGroupUnits()) {
		for (CompetenceCourse competenceCourse : competenceCourseGroupUnit.getCompetenceCourses()) {
		    if (competenceCourse.getCurricularStage().equals(curricularStage)) {
			result.add(competenceCourse);
		    }
		}
	    }	   
	}
	return result;
    }
    
    public List<ScientificAreaUnit> getScientificAreaUnits() {
	final SortedSet<ScientificAreaUnit> result = new TreeSet<ScientificAreaUnit>(ScientificAreaUnit.COMPARATOR_BY_NAME_AND_ID);
	for (Unit unit : getSubUnits()) {
	    if (unit.isScientificAreaUnit()) {
		result.add((ScientificAreaUnit) unit);
	    }
	}
	return new ArrayList<ScientificAreaUnit>(result);
    }   
           
    @Override
    public Accountability addParentUnit(Unit parentUnit, AccountabilityType accountabilityType) {
	if (getDepartment() == null) {
	    if (parentUnit != null
		    && (!parentUnit.isOfficialExternal() || (!parentUnit.isCountryUnit()
			    && !parentUnit.isSchoolUnit() && !parentUnit.isUniversityUnit()))) {
		throw new DomainException("error.unit.invalid.parentUnit");
	    }	   
	} else {
	    if(parentUnit != null && !parentUnit.isInternal()) {
		throw new DomainException("error.unit.invalid.parentUnit");
	    }	    
	}
	return super.addParentUnit(parentUnit, accountabilityType);
    }
    
    @Override
    public void setAcronym(String acronym) {
        if(StringUtils.isEmpty(acronym)) {
            throw new DomainException("error.unit.empty.acronym");
        }
	super.setAcronym(acronym);
    }
    
    @Override
    public void setDepartment(Department department) {
	if(department == null) {
	    throw new DomainException("error.departmentUnit.empty.department");
	}
	super.setDepartment(department);
    }
    
    @Override
    public void setType(PartyTypeEnum partyTypeEnum) {
        throw new DomainException("unit.impossible.set.type");
    }
    
    @Override
    public boolean isDepartmentUnit() {
        return true;
    }
    
    @Override
    public boolean hasCompetenceCourses(final CompetenceCourse competenceCourse) {
	for (Unit subUnit : getSubUnits()) {
	    if(subUnit.hasCompetenceCourses(competenceCourse)) {
		return true;
	    }
	}
	return false;
    }
    
    @Override
    public void delete() {
	for (; !getParticipatingAnyCurricularCourseCurricularRules().isEmpty(); getParticipatingAnyCurricularCourseCurricularRules().get(0).delete());		
	super.setDepartment(null);	
	super.delete();	
    }    
    
    private static void checkIfAlreadyExistsOneDepartmentUnitWithSameAcronymAndName(DepartmentUnit departmentUnit) {
	if (departmentUnit.getDepartment() == null) {
	    for (Unit parentUnit : departmentUnit.getParentUnits()) {
		for (Unit subUnit : parentUnit.getAllSubUnits()) {
		    if (!subUnit.equals(departmentUnit)
			    && subUnit.isDepartmentUnit()
			    && (departmentUnit.getName().equalsIgnoreCase(subUnit.getName()) || departmentUnit
				    .getAcronym().equalsIgnoreCase(subUnit.getAcronym()))) {
			throw new DomainException("error.unit.already.exists.unit.with.same.name.or.acronym");
		    }
		}  
	    }	   	  
	} else {
	    for (Unit unit : UnitUtils.readInstitutionUnit().getAllSubUnits()) {
		if (!unit.equals(departmentUnit) && unit.isDepartmentUnit() &&
			(departmentUnit.getAcronym().equalsIgnoreCase(unit.getAcronym()) || 
				departmentUnit.getName().equalsIgnoreCase(unit.getName()))) {
		    throw new DomainException("error.unit.already.exists.unit.with.same.name.or.acronym");
		}
	    }
	}
    }
    
    public boolean isUserAbleToDefineGroups(Person person) {
		UnitSite site = this.getSite();
		return (site == null) ? false : site.getManagers().contains(person); 
	}
	
	public boolean isCurrentUserAbleToDefineGroups() {
		return isUserAbleToDefineGroups(AccessControl.getPerson());
	}
	
	public List<IGroup> getDefaultGroups() {
		List<IGroup> groups = new ArrayList<IGroup>();
		ExecutionYear currentYear = ExecutionYear.readCurrentExecutionYear();
		Department department = this.getDepartment();
		groups.add(new DepartmentTeachersByExecutionYearGroup(currentYear, department));
		groups.add(new DepartmentStudentsByExecutionYearGroup(currentYear, department));
		groups.add(new DepartmentEmployeesByExecutionYearGroup(currentYear, department));
		return groups;
	}
	
	public List<IGroup> getGroups() {
		List<IGroup> groups = new ArrayList<IGroup>();
		groups.addAll(this.getUserDefinedGroups());
		groups.addAll(this.getDefaultGroups());
		return groups;
	}
}
