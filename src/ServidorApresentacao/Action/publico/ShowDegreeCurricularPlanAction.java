package ServidorApresentacao.Action.publico;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.InfoCurricularCourseScope;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixContextDispatchAction;

/**
 * @author T�nia Pous�o Created on 9/Out/2003
 */
public class ShowDegreeCurricularPlanAction extends FenixContextDispatchAction
{

    public ActionForward showCurricularPlan(
        ActionMapping mapping,
        ActionForm actionForm,
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception
    {
        ActionErrors errors = new ActionErrors();

        Integer executionPeriodOId = getFromRequest("executionPeriodOID", request);

        Integer degreeId = getFromRequest("degreeID", request);
        request.setAttribute("degreeID", degreeId);

        Integer degreeCurricularPlanId = getFromRequest("degreeCurricularPlanID", request);
        request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanId);

        Boolean inEnglish = getFromRequestBoolean("inEnglish", request);
        request.setAttribute("inEnglish", inEnglish);

        GestorServicos gestorServicos = GestorServicos.manager();
        Object[] args = { degreeCurricularPlanId };

        List activeCurricularCourseScopes = null;
        try
        {
            activeCurricularCourseScopes =
                (List) gestorServicos.executar(null, "ReadActiveDegreeCurricularPlanByID", args);
        } catch (FenixServiceException e)
        {
            errors.add("impossibleCurricularPlan", new ActionError("error.impossibleCurricularPlan"));
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
        }
        if (activeCurricularCourseScopes == null || activeCurricularCourseScopes.size() <= 0)
        {
            errors.add(
                "noDegreeCurricularPlan",
                new ActionError("error.coordinator.noDegreeCurricularPlan"));
            saveErrors(request, errors);
        }

        //order list by year, next semester, next course
        ComparatorChain comparatorChain = new ComparatorChain();
        comparatorChain.addComparator(
            new BeanComparator("infoCurricularSemester.infoCurricularYear.year"));
        comparatorChain.addComparator(new BeanComparator("infoCurricularSemester.semester"));
        comparatorChain.addComparator(new BeanComparator("infoCurricularCourse.name"));
        Collections.sort(activeCurricularCourseScopes, comparatorChain);

        request.setAttribute("allActiveCurricularCourseScopes", activeCurricularCourseScopes);
		request.setAttribute("infoDegreeCurricularPlan", ((InfoCurricularCourseScope)activeCurricularCourseScopes.get(0)).getInfoCurricularCourse().getInfoDegreeCurricularPlan());
        
        if (inEnglish == null || inEnglish.booleanValue() == false)
        {
            return mapping.findForward("showDegreeCurricularPlan");
        } else
        {
            return mapping.findForward("showDegreeCurricularPlanEnglish");
        }
    }

    private Integer getFromRequest(String parameter, HttpServletRequest request)
    {
        Integer parameterCode = null;
        String parameterCodeString = request.getParameter(parameter);
        if (parameterCodeString == null)
        {
            parameterCodeString = (String) request.getAttribute(parameter);
        }
        if (parameterCodeString != null)
        {
            parameterCode = new Integer(parameterCodeString);
        }
        return parameterCode;
    }

    private Boolean getFromRequestBoolean(String parameter, HttpServletRequest request)
    {
        Boolean parameterBoolean = null;

        String parameterCodeString = request.getParameter(parameter);
        if (parameterCodeString == null)
        {
            parameterCodeString = (String) request.getAttribute(parameter);
        }
        if (parameterCodeString != null)
        {
            parameterBoolean = new Boolean(parameterCodeString);
        }

        return parameterBoolean;
    }
}