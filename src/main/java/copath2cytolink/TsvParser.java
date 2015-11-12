/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package copath2cytolink;

import copath2cytolink.data.CWS;
import copath2cytolink.data.ObjectFactory;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import org.supercsv.io.CsvMapReader;
import org.supercsv.io.ICsvMapReader;
import org.supercsv.prefs.CsvPreference;

/**
 *
 * @author ghsmith
 */
public class TsvParser {

    public static void main(String[] args) throws FileNotFoundException, IOException, PropertyException, JAXBException {

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        
        ObjectFactory of = new ObjectFactory();
        
        ICsvMapReader mapReader = new CsvMapReader(new FileReader(args[0]), CsvPreference.TAB_PREFERENCE);
        final String[] header = mapReader.getHeader(true);
        Map<String, String> tsvMap;
        while((tsvMap = mapReader.read(header)) != null ) {
            
            CWS cws = of.createCWS();
            cws.setCase(of.createCWSCase());
            cws.getCase().setName(tsvMap.get("specnum_formatted"));
            cws.getCase().setCaseDetailsSet(of.createCWSCaseCaseDetailsSet());
            {
                CWS.Case.CaseDetailsSet.CaseDetail caseDetail = of.createCWSCaseCaseDetailsSetCaseDetail();
                caseDetail.setTitle("order date");
                caseDetail.setText(sdf.format(new Date(tsvMap.get("order_date"))));
                caseDetail.setType("system");
                caseDetail.setCtrltype("Date");
                caseDetail.setEditable("True");
                caseDetail.setMandatory("False");
                cws.getCase().getCaseDetailsSet().getCaseDetail().add(caseDetail);
            }
            {
                CWS.Case.CaseDetailsSet.CaseDetail caseDetail = of.createCWSCaseCaseDetailsSetCaseDetail();
                caseDetail.setTitle("accession date");
                caseDetail.setText(sdf.format(new Date(tsvMap.get("accession_date"))));
                caseDetail.setType("system");
                caseDetail.setCtrltype("Date");
                caseDetail.setEditable("True");
                caseDetail.setMandatory("False");
                cws.getCase().getCaseDetailsSet().getCaseDetail().add(caseDetail);
            }
            {
                CWS.Case.CaseDetailsSet.CaseDetail caseDetail = of.createCWSCaseCaseDetailsSetCaseDetail();
                caseDetail.setTitle("last name");
                caseDetail.setText(tsvMap.get("lastname"));
                caseDetail.setType("system");
                caseDetail.setCtrltype("Text");
                caseDetail.setEditable("True");
                caseDetail.setMandatory("False");
                cws.getCase().getCaseDetailsSet().getCaseDetail().add(caseDetail);
            }
            {
                CWS.Case.CaseDetailsSet.CaseDetail caseDetail = of.createCWSCaseCaseDetailsSetCaseDetail();
                caseDetail.setTitle("first name");
                caseDetail.setText(tsvMap.get("firstname"));
                caseDetail.setType("system");
                caseDetail.setCtrltype("Text");
                caseDetail.setEditable("True");
                caseDetail.setMandatory("False");
                cws.getCase().getCaseDetailsSet().getCaseDetail().add(caseDetail);
            }
            {
                CWS.Case.CaseDetailsSet.CaseDetail caseDetail = of.createCWSCaseCaseDetailsSetCaseDetail();
                caseDetail.setTitle("middle name");
                caseDetail.setText(tsvMap.get("middlename"));
                caseDetail.setType("system");
                caseDetail.setCtrltype("Text");
                caseDetail.setEditable("True");
                caseDetail.setMandatory("False");
                cws.getCase().getCaseDetailsSet().getCaseDetail().add(caseDetail);
            }
            {
                CWS.Case.CaseDetailsSet.CaseDetail caseDetail = of.createCWSCaseCaseDetailsSetCaseDetail();
                caseDetail.setTitle("date of birth");
                caseDetail.setText(sdf.format(new Date(tsvMap.get("date_of_birth"))));
                caseDetail.setType("system");
                caseDetail.setCtrltype("Text");
                caseDetail.setEditable("True");
                caseDetail.setMandatory("False");
                cws.getCase().getCaseDetailsSet().getCaseDetail().add(caseDetail);
            }
            {
                CWS.Case.CaseDetailsSet.CaseDetail caseDetail = of.createCWSCaseCaseDetailsSetCaseDetail();
                caseDetail.setTitle("part type name");
                caseDetail.setText(tsvMap.get("part_type_name"));
                caseDetail.setType("system");
                caseDetail.setCtrltype("Text");
                caseDetail.setEditable("True");
                caseDetail.setMandatory("False");
                cws.getCase().getCaseDetailsSet().getCaseDetail().add(caseDetail);
            }

            File xmlFile = new File(cws.getCase().getName() + ".xml");
            OutputStream xmlOutputStream = new FileOutputStream(xmlFile);
            JAXBContext jc = JAXBContext.newInstance("copath2cytolink.data");
            Marshaller m = jc.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, new Boolean(true));
            m.marshal(cws, xmlOutputStream);        
            xmlOutputStream.close();            
            
        }
        mapReader.close();
        
    }
    
}
