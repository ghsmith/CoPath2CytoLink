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
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.logging.Logger;
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

    private static final Logger LOGGER = Logger.getLogger(TsvParser.class.getName());

    public static void main(String[] args) throws FileNotFoundException, IOException, PropertyException, JAXBException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        
        ObjectFactory of = new ObjectFactory();

        int tsvFileLineCount;
        {
            FileReader tsvFileReader = new FileReader(args[0]);
            LineNumberReader lnr = new LineNumberReader(tsvFileReader);
            while(lnr.skip(Long.MAX_VALUE) > 0) {}
            tsvFileLineCount = lnr.getLineNumber() - 1;
            tsvFileReader.close();
        }
        
        ICsvMapReader mapReader = new CsvMapReader(new FileReader(args[0]), CsvPreference.TAB_PREFERENCE);
        final String[] header = mapReader.getHeader(true);
        Map<String, String> tsvMap;
        int x = 0;
        while((tsvMap = mapReader.read(header)) != null ) {
            
            x++;
            LOGGER.info("processing case " + tsvMap.get("specnum_formatted") + " (" + x + "/" + tsvFileLineCount + ")");
            
            CWS cws = of.createCWS();
            cws.setCase(of.createCWSCase());
            cws.getCase().setName(tsvMap.get("specnum_formatted"));
            cws.getCase().setCaseDetailsSet(of.createCWSCaseCaseDetailsSet());
            {
                CWS.Case.CaseDetailsSet.CaseDetail caseDetail = of.createCWSCaseCaseDetailsSetCaseDetail();
                caseDetail.setTitle("Patient name");
                caseDetail.setText(tsvMap.get("lastname") + ", " + tsvMap.get("firstname") + (tsvMap.get("middlename") != null ? " " + tsvMap.get("middlename") : ""));
                caseDetail.setType("system");
                caseDetail.setCtrltype("Text");
                caseDetail.setEditable("True");
                caseDetail.setMandatory("False");
                cws.getCase().getCaseDetailsSet().getCaseDetail().add(caseDetail);
            }
            {
                CWS.Case.CaseDetailsSet.CaseDetail caseDetail = of.createCWSCaseCaseDetailsSetCaseDetail();
                caseDetail.setTitle("Date of birth");
                caseDetail.setText(sdf.format(new Date(tsvMap.get("date_of_birth"))));
                caseDetail.setType("system");
                caseDetail.setCtrltype("Date");
                caseDetail.setEditable("True");
                caseDetail.setMandatory("False");
                cws.getCase().getCaseDetailsSet().getCaseDetail().add(caseDetail);
            }
            {
                CWS.Case.CaseDetailsSet.CaseDetail caseDetail = of.createCWSCaseCaseDetailsSetCaseDetail();
                caseDetail.setTitle("Specimen type");
                caseDetail.setText(tsvMap.get("part_type_name"));
                caseDetail.setType("system");
                caseDetail.setCtrltype("Text");
                caseDetail.setEditable("True");
                caseDetail.setMandatory("False");
                cws.getCase().getCaseDetailsSet().getCaseDetail().add(caseDetail);
            }
            {
                CWS.Case.CaseDetailsSet.CaseDetail caseDetail = of.createCWSCaseCaseDetailsSetCaseDetail();
                caseDetail.setTitle("Referral reason");
                caseDetail.setText("");
                caseDetail.setType("system");
                caseDetail.setCtrltype("Text");
                caseDetail.setEditable("True");
                caseDetail.setMandatory("False");
                cws.getCase().getCaseDetailsSet().getCaseDetail().add(caseDetail);
            }
            {
                CWS.Case.CaseDetailsSet.CaseDetail caseDetail = of.createCWSCaseCaseDetailsSetCaseDetail();
                caseDetail.setTitle("Technologist");
                caseDetail.setText("");
                caseDetail.setType("system");
                caseDetail.setCtrltype("Text");
                caseDetail.setEditable("True");
                caseDetail.setMandatory("False");
                cws.getCase().getCaseDetailsSet().getCaseDetail().add(caseDetail);
            }
            {
                CWS.Case.CaseDetailsSet.CaseDetail caseDetail = of.createCWSCaseCaseDetailsSetCaseDetail();
                caseDetail.setTitle("Date");
                caseDetail.setText(sdf.format(new Date(tsvMap.get("order_date"))));
                caseDetail.setType("system");
                caseDetail.setCtrltype("Date");
                caseDetail.setEditable("True");
                caseDetail.setMandatory("False");
                cws.getCase().getCaseDetailsSet().getCaseDetail().add(caseDetail);
            }
            {
                CWS.Case.CaseDetailsSet.CaseDetail caseDetail = of.createCWSCaseCaseDetailsSetCaseDetail();
                caseDetail.setTitle("Case comment");
                caseDetail.setText("");
                caseDetail.setType("system");
                caseDetail.setCtrltype("Text");
                caseDetail.setEditable("True");
                caseDetail.setMandatory("False");
                cws.getCase().getCaseDetailsSet().getCaseDetail().add(caseDetail);
            }
            {
                CWS.Case.CaseDetailsSet.CaseDetail caseDetail = of.createCWSCaseCaseDetailsSetCaseDetail();
                caseDetail.setTitle("Result");
                caseDetail.setText("");
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
