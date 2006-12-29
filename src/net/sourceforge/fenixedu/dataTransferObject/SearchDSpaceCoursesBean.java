package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;

public class SearchDSpaceCoursesBean extends SearchDSpaceBean{
	
	
	DomainReference<ExecutionYear> executionYear;
	DomainReference<ExecutionPeriod> executionPeriod;
	
	public SearchDSpaceCoursesBean() {
		super();
		this.setExecutionYear(null);
	} 
		
	public ExecutionYear getExecutionYear() {
		return executionYear.getObject();
	}

	public void setExecutionYear(ExecutionYear executionYear) {
		this.executionYear = new DomainReference<ExecutionYear>(executionYear);
	}
	
	public ExecutionPeriod getExecutionPeriod() {
		return executionPeriod.getObject();
	}

	public void setExecutionPeriod(ExecutionPeriod executionPeriod) {
		this.executionPeriod = new DomainReference<ExecutionPeriod>(executionPeriod);
	}
}
