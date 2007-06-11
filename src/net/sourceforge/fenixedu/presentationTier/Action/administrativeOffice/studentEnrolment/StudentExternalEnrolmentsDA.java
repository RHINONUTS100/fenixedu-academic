package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.studentEnrolment;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.externalUnits.EditExternalEnrolmentBean;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.externalUnits.ExternalCurricularCourseResultBean;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.studentEnrolment.ExternalCurricularCourseEnrolmentBean;
import net.sourceforge.fenixedu.domain.ExternalCurricularCourse;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitUtils;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.studentCurriculum.ExternalEnrolment;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

public class StudentExternalEnrolmentsDA extends FenixDispatchAction {
    
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	
	request.setAttribute("contextInformation", getContextInformation());
	request.setAttribute("parameters", getParameters(request));
	return super.execute(mapping, actionForm, request, response);
    }
    
    public ActionForward manageExternalEnrolments(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	
	request.setAttribute("student", getStudent(request, actionForm));
	return mapping.findForward("manageExternalEnrolments");
    }

    public ActionForward chooseExternalUnit(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	
	request.setAttribute("student", getStudent(request, actionForm));
	request.setAttribute("unit", UnitUtils.readEarthUnit());
	return mapping.findForward("chooseExternalUnit");
    }

    public ActionForward chooseExternalCurricularCourses(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {

	final Unit externalUnit = getExternalUnit(request, actionForm);
	
	request.setAttribute("externalUnit", externalUnit);
	request.setAttribute("externalCurricularCourseBeans", buildExternalCurricularCourseResultBeans(externalUnit));
	request.setAttribute("student", getStudent(request, actionForm));
	
	return mapping.findForward("chooseExternalCurricularCourses");
    }
    
    private Set<ExternalCurricularCourseResultBean> buildExternalCurricularCourseResultBeans(final Unit unit) {
	final Set<ExternalCurricularCourseResultBean> result = new TreeSet<ExternalCurricularCourseResultBean>(new BeanComparator("fullName"));
	for (final ExternalCurricularCourse externalCurricularCourse : unit.getAllExternalCurricularCourses()) {
	    result.add(new ExternalCurricularCourseResultBean(externalCurricularCourse));
	}
	return result;
    }
    
    public ActionForward prepareCreateExternalEnrolments(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {

	final String[] externalCurricularCourseIDs = ((DynaActionForm) actionForm).getStrings("selectedExternalCurricularCourses");
	if (externalCurricularCourseIDs == null || externalCurricularCourseIDs.length == 0) {
	    addActionMessage(request, "error.StudentEnrolmentDA.must.choose.externalCurricularCourses");
	    return chooseExternalCurricularCourses(mapping, actionForm, request, response);	    
	}
	
	request.setAttribute("externalCurricularCourseEnrolmentBeans", buildExternalCurricularCourseEnrolmentBeans(externalCurricularCourseIDs));
	request.setAttribute("student", getStudent(request, actionForm));
	request.setAttribute("externalUnit", getExternalUnit(request, actionForm));
	
	return mapping.findForward("createExternalEnrolments");
    }

    private List<ExternalCurricularCourseEnrolmentBean> buildExternalCurricularCourseEnrolmentBeans(final String[] externalCurricularCourseIDs) {
	final List<ExternalCurricularCourseEnrolmentBean> result = new ArrayList<ExternalCurricularCourseEnrolmentBean>(externalCurricularCourseIDs.length);
	for (final String externalCurricularCourseID : externalCurricularCourseIDs) {
	    result.add(new ExternalCurricularCourseEnrolmentBean(getExternalCurricularCourseByID(externalCurricularCourseID)));
	}
	return result;
    }

    public ActionForward createExternalEnrolmentsInvalid(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {

	request.setAttribute("externalCurricularCourseEnrolmentBeans", getRenderedObject());
	request.setAttribute("student", getStudent(request, actionForm));
	request.setAttribute("externalUnit", getExternalUnit(request, actionForm));
	
	return mapping.findForward("createExternalEnrolments");
    }
    
    public ActionForward createExternalEnrolments(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	final List<ExternalCurricularCourseEnrolmentBean> externalCurricularCourseEnrolmentBeans = (List<ExternalCurricularCourseEnrolmentBean>) getRenderedObject("externalCurricularCourseEnrolmentBeans");
	final Student student = getStudent(request, actionForm);
	
	try {
	    executeService("CreateExternalEnrolments", new Object[] {student, externalCurricularCourseEnrolmentBeans});
	    
	} catch (NotAuthorizedException e) {
	    addActionMessage("error", request, "error.notAuthorized");
	    request.setAttribute("student", getStudent(request, actionForm));
	    request.setAttribute("externalCurricularCourseEnrolmentBeans", externalCurricularCourseEnrolmentBeans);
	    request.setAttribute("externalUnit", getExternalUnit(request, actionForm));
	    return mapping.findForward("createExternalEnrolments");
	    
	} catch (DomainException e) {
	    addActionMessage("error", request, e.getMessage(), e.getArgs());
	    request.setAttribute("student", getStudent(request, actionForm));
	    request.setAttribute("externalCurricularCourseEnrolmentBeans", externalCurricularCourseEnrolmentBeans);
	    request.setAttribute("externalUnit", getExternalUnit(request, actionForm));
	    return mapping.findForward("createExternalEnrolments");
	}
	
	return backToMainPage(mapping, actionForm, request, response);
    }
    
    public ActionForward deleteExternalEnrolments(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	
	final String[] externalEnrolmentIDs = ((DynaActionForm) actionForm).getStrings("externalEnrolmentsToDelete");
	final Student student = getStudent(request, actionForm);
	request.setAttribute("student", student);
	
	try {
	    executeService("DeleteExternalEnrolments", new Object[] {student, externalEnrolmentIDs});
	} catch (NotAuthorizedException e) {
	    addActionMessage("error", request, "error.notAuthorized");
	} catch (DomainException e) {
	    addActionMessage("error", request, e.getMessage(), e.getArgs());
	}
	
	return mapping.findForward("manageExternalEnrolments");
    }
    
    public ActionForward prepareEditExternalEnrolment(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {

	final ExternalEnrolment externalEnrolment = getExternalEnrolment(request, actionForm);
	request.setAttribute("student", externalEnrolment.getStudent());
	request.setAttribute("externalEnrolmentBean", new EditExternalEnrolmentBean(externalEnrolment));
	return mapping.findForward("prepareEditExternalEnrolment");
    }
    
    public ActionForward editExternalEnrolment(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	final EditExternalEnrolmentBean externalEnrolmentBean = (EditExternalEnrolmentBean) getRenderedObject();
	final ExternalEnrolment externalEnrolment = externalEnrolmentBean.getExternalEnrolment();
	try {
	    executeService("EditExternalEnrolment", new Object[] {externalEnrolmentBean, externalEnrolment.getStudent()});
	    return manageExternalEnrolments(mapping, actionForm, request, response);
	    
	} catch (final NotAuthorizedException e) {
	    addActionMessage("error", request, "error.notAuthorized");
	} catch (final DomainException e) {
	    addActionMessage("error", request, e.getMessage());
	}

	request.setAttribute("externalEnrolmentBean", externalEnrolmentBean);
	return mapping.findForward("prepareEditExternalEnrolment");	
    }
    
    public ActionForward backToMainPage(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {
	return manageExternalEnrolments(mapping, actionForm, request, response);
    }
    
    public ActionForward cancelExternalEnrolment(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {
	request.setAttribute("student", getStudent(request, actionForm));
	return mapping.findForward("viewStudentDetails");
    }
 
    protected String getContextInformation() {
	return "/studentExternalEnrolments.do?";
    }
    
    protected String getParameters(final HttpServletRequest request) {
	return StringUtils.EMPTY;
    }
    
    protected Student getStudent(final HttpServletRequest request, ActionForm form) {
	return rootDomainObject.readStudentByOID(getIntegerFromRequestOrForm(request, (DynaActionForm) form, "studentId"));
    }
    
    protected Unit getExternalUnit(final HttpServletRequest request, ActionForm actionForm) {
	return (Unit) rootDomainObject.readPartyByOID(getIntegerFromRequestOrForm(request, (DynaActionForm) actionForm, "externalUnitId"));
    }
    
    protected ExternalCurricularCourse getExternalCurricularCourseByID(final String externalCurricularCourseID) {
	return rootDomainObject.readExternalCurricularCourseByOID(Integer.valueOf(externalCurricularCourseID));
    }
    
    protected ExternalEnrolment getExternalEnrolment(final HttpServletRequest request, ActionForm actionForm) {
	return rootDomainObject.readExternalEnrolmentByOID(getIntegerFromRequestOrForm(request, (DynaActionForm) actionForm, "externalEnrolmentId"));
    }
}
