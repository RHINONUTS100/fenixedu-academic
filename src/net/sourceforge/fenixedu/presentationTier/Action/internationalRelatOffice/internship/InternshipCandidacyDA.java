package net.sourceforge.fenixedu.presentationTier.Action.internationalRelatOffice.internship;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.internationalRelationsOffice.internship.CandidateSearchBean;
import net.sourceforge.fenixedu.dataTransferObject.internship.InternshipCandidacyBean;
import net.sourceforge.fenixedu.domain.internship.DuplicateInternshipCandidacy;
import net.sourceforge.fenixedu.domain.internship.InternshipCandidacy;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.util.report.Spreadsheet;
import net.sourceforge.fenixedu.util.report.Spreadsheet.Row;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

/**
 * @author Pedro Santos (pmrsa)
 */
@Mapping(path = "/internship/internshipCandidacy", module = "internationalRelatOffice")
@Forwards( { @Forward(name = "candidates", path = "/internationalRelatOffice/internship/candidacy/listCandidates.jsp"),
	@Forward(name = "view", path = "/internationalRelatOffice/internship/candidacy/viewCandidate.jsp"),
	@Forward(name = "edit", path = "/internationalRelatOffice/internship/candidacy/editCandidate.jsp") })
public class InternshipCandidacyDA extends FenixDispatchAction {
    private static final ResourceBundle ENUMERATION_RESOURCES = ResourceBundle.getBundle("resources/EnumerationResources",
	    new Locale("pt"));

    private static final String[] HEADERS = new String[] { "N� Candidatura", "Universidade", "N� Aluno", "Ano", "Curso", "Nome",
	    "Sexo", "Data Nascimento", "Naturalidade", "Nacionalidade", "B.I.", "Arquivo", "Emiss�o", "Validade", "Passaporte",
	    "Arquivo", "Emiss�o", "Validade", "Morada", "Cod. Postal", "Localidade", "Telefone", "Telem�vel", "e-mail",
	    "1� Prefer�ncia", "2� Prefer�ncia", "3� Prefer�ncia", "Ingl�s", "Franc�s", "Espanhol", "Alem�o", "Candidatura Pr�via" };

    private static final String[] HEADERS_NO_UNIV = new String[] { "N� Candidatura", "N� Aluno", "Ano", "Curso", "Nome", "Sexo",
	    "Data Nascimento", "Naturalidade", "Nacionalidade", "B.I.", "Arquivo", "Emiss�o", "Validade", "Passaporte",
	    "Arquivo", "Emiss�o", "Validade", "Morada", "Cod. Postal", "Localidade", "Telefone", "Telem�vel", "e-mail",
	    "1� Prefer�ncia", "2� Prefer�ncia", "3� Prefer�ncia", "Ingl�s", "Franc�s", "Espanhol", "Alem�o", "Candidatura Pr�via" };

    public ActionForward prepareCandidates(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	CandidateSearchBean search = new CandidateSearchBean();
	request.setAttribute("search", search);
	request.setAttribute("candidates", filterCandidates(search));
	return mapping.findForward("candidates");
    }

    public ActionForward searchCandidates(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	CandidateSearchBean search = (CandidateSearchBean) getRenderedObject("search");
	if (search.getCutEnd().isBefore(search.getCutStart())) {
	    addActionMessage(request, "error.internationalrelations.internship.candidacy.search.startafterend");
	    return prepareCandidates(mapping, actionForm, request, response);
	}
	request.setAttribute("search", search);
	request.setAttribute("candidates", filterCandidates(search));
	return mapping.findForward("candidates");
    }

    public ActionForward candidateView(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	long oid = Long.parseLong(request.getParameter("candidacy.OID"));
	request.setAttribute("candidacy", new InternshipCandidacyBean((InternshipCandidacy) InternshipCandidacy.fromOID(oid)));
	return mapping.findForward("view");
    }

    public ActionForward prepareCandidacyEdit(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	long oid = Long.parseLong(request.getParameter("candidacy.OID"));
	request.setAttribute("candidacy", new InternshipCandidacyBean((InternshipCandidacy) InternshipCandidacy.fromOID(oid)));
	return mapping.findForward("edit");
    }

    public ActionForward candidateEdit(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	InternshipCandidacyBean bean = (InternshipCandidacyBean) getRenderedObject();
	try {
	    bean.getCandidacy().edit(bean);
	} catch (DuplicateInternshipCandidacy e) {
	    addActionMessage(request, "error.internationalrelations.internship.candidacy.duplicateStudentNumber", e.getNumber(),
		    e.getUniversity());
	    return mapping.findForward("edit");
	}
	return prepareCandidates(mapping, actionForm, request, response);
    }

    public ActionForward exportToCandidatesToXls(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws FenixActionException {
	CandidateSearchBean search = (CandidateSearchBean) getRenderedObject("search");
	if (search.getCutEnd().isBefore(search.getCutStart())) {
	    addActionMessage(request, "error.internationalrelations.internship.candidacy.search.startafterend");
	    return prepareCandidates(mapping, actionForm, request, response);
	}
	if (search.getCutEnd().plusDays(1).toDateMidnight().isAfterNow()) {
	    addActionMessage(request, "error.internationalrelations.internship.candidacy.export.todaywontwork");
	    return prepareCandidates(mapping, actionForm, request, response);
	}
	Spreadsheet sheet = new Spreadsheet(search.getName());
	if (search.getUniversity() == null) {
	    sheet.setHeaders(HEADERS);
	} else {
	    sheet.setHeaders(HEADERS_NO_UNIV);
	}
	for (InternshipCandidacyBean bean : filterCandidates(search)) {
	    Row row = sheet.addRow();
	    row.setCell(bean.getCandidacy().getCandidacyCode());
	    if (search.getUniversity() == null) {
		row.setCell(bean.getUniversity().getFullPresentationName());
	    }
	    row.setCell(bean.getStudentNumber());
	    row.setCell(bean.getStudentYear().ordinal() + 1);
	    row.setCell(bean.getDegree());
	    row.setCell(bean.getName());
	    row.setCell(bean.getGender().toLocalizedString());
	    row.setCell(bean.getBirthday().toString("dd-MM-yyyy"));
	    row.setCell(bean.getParishOfBirth());
	    row.setCell(StringUtils.capitalize(bean.getCountryOfBirth().getCountryNationality().getPreferedContent()
		    .toLowerCase()));
	    row.setCell(bean.getDocumentIdNumber());
	    row.setCell(bean.getEmissionLocationOfDocumentId());
	    row.setCell(bean.getEmissionDateOfDocumentId().toString("dd-MM-yyyy"));
	    row.setCell(bean.getExpirationDateOfDocumentId().toString("dd-MM-yyyy"));
	    row.setCell(bean.getPassportIdNumber() != null ? bean.getPassportIdNumber() : "");
	    row.setCell(bean.getEmissionLocationOfPassport() != null ? bean.getEmissionLocationOfPassport() : "");
	    row.setCell(bean.getEmissionDateOfPassport() != null ? bean.getEmissionDateOfPassport().toString("dd-MM-yyyy") : "");
	    row.setCell(bean.getExpirationDateOfPassport() != null ? bean.getExpirationDateOfPassport().toString("dd-MM-yyyy")
		    : "");
	    row.setCell(bean.getStreet());
	    row.setCell(bean.getAreaCode());
	    row.setCell(bean.getArea());
	    row.setCell(bean.getTelephone());
	    row.setCell(bean.getMobilePhone());
	    row.setCell(bean.getEmail());
	    row.setCell(StringUtils.capitalize(bean.getFirstDestination() != null ? bean.getFirstDestination().getName()
		    .toLowerCase() : ""));
	    row.setCell(StringUtils.capitalize(bean.getSecondDestination() != null ? bean.getSecondDestination().getName()
		    .toLowerCase() : ""));
	    row.setCell(StringUtils.capitalize(bean.getThirdDestination() != null ? bean.getThirdDestination().getName()
		    .toLowerCase() : ""));
	    row.setCell(ENUMERATION_RESOURCES.getString(bean.getEnglish().getQualifiedKey()));
	    row.setCell(ENUMERATION_RESOURCES.getString(bean.getFrench().getQualifiedKey()));
	    row.setCell(ENUMERATION_RESOURCES.getString(bean.getSpanish().getQualifiedKey()));
	    row.setCell(ENUMERATION_RESOURCES.getString(bean.getGerman().getQualifiedKey()));
	    row.setCell(bean.getPreviousCandidacy() ? "Sim" : "N�o");
	}

	response.setContentType("application/vnd.ms-excel");
	response.setHeader("Content-disposition", "attachment; filename=" + search.getName() + ".xls");

	try {
	    OutputStream outputStream = response.getOutputStream();
	    sheet.exportToXLSSheet(outputStream);
	    outputStream.close();
	} catch (IOException e) {
	    e.printStackTrace();
	}
	return null;
    }

    private SortedSet<InternshipCandidacyBean> filterCandidates(CandidateSearchBean search) {
	SortedSet<InternshipCandidacyBean> candidates = new TreeSet<InternshipCandidacyBean>();
	for (InternshipCandidacy candidacy : rootDomainObject.getInternshipCandidacySet()) {
	    if (isIncluded(candidacy, search)) {
		candidates.add(new InternshipCandidacyBean(candidacy));
	    }
	}
	return candidates;
    }

    private boolean isIncluded(InternshipCandidacy candidacy, CandidateSearchBean search) {
	if (search.getUniversity() != null && !candidacy.getUniversity().equals(search.getUniversity()))
	    return false;
	if (search.getCutStart() != null && candidacy.getCandidacyDate().isBefore(search.getCutStart().toDateMidnight()))
	    return false;
	if (search.getCutEnd() != null && !candidacy.getCandidacyDate().isBefore(search.getCutEnd().plusDays(1).toDateMidnight()))
	    return false;
	return true;
    }
}
