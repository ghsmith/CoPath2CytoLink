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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
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

        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        
        ObjectFactory of = new ObjectFactory();

        ICsvMapReader mapReader = new CsvMapReader(new FileReader(args[0]), CsvPreference.TAB_PREFERENCE);
        final String[] header = mapReader.getHeader(true);
        
        // create uniqueness in column names
        {
            Map<String, Integer> columnNameMap = new HashMap<>();
            for(int x = 0; x < header.length; x++) {
                Integer columnNameCount = (columnNameMap.get(header[x]) == null ? 0 : columnNameMap.get(header[x]));
                columnNameMap.put(header[x], columnNameCount + 1);
                header[x] = header[x] + (columnNameCount == 0 ? "" : columnNameCount.toString());
            }
        }
        
        Map<String, CWS> caseMap = new HashMap<>();
        Map<String, Set<String>> casePartNameMap = new HashMap<>();
        Map<String, String> tsvMap;
        while((tsvMap = mapReader.read(header)) != null ) {
            
            LOGGER.info("reading case " + tsvMap.get("specnum_formatted"));

            CWS cws;
            if(caseMap.get(tsvMap.get("specnum_formatted")) == null) {

                cws = of.createCWS();
                caseMap.put(tsvMap.get("specnum_formatted"), cws);

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
                    casePartNameMap.put(tsvMap.get("specnum_formatted"), new HashSet<String>());
                    casePartNameMap.get(tsvMap.get("specnum_formatted")).add(tsvMap.get("name"));
                }
                {
                    CWS.Case.CaseDetailsSet.CaseDetail caseDetail = of.createCWSCaseCaseDetailsSetCaseDetail();
                    caseDetail.setTitle("Referral reason");
                    caseDetail.setText(tsvMap.get("Text").replace("\n", "/"));
                    if(caseDetail.getText() != null && caseDetail.getText().endsWith("/")) {
                        caseDetail.setText(caseDetail.getText().substring(0, caseDetail.getText().length() - 1));
                    }
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
                {
                    CWS.Case.ScanSlide scanSlide = of.createCWSCaseScanSlide();
                    scanSlide.setBarcode(tsvMap.get("specnum_formatted"));
                    scanSlide.setTemplatename("BM");
                    cws.getCase().setScanSlide(scanSlide);
                }
                {
                    CWS.Case.ScanSlide.ScanDetailsSet scanSlideDetailSet = of.createCWSCaseScanSlideScanDetailsSet();
                    cws.getCase().getScanSlide().setScanDetailsSet(scanSlideDetailSet);
                }
                {
                    CWS.Case.ScanSlide.ScanDetailsSet.SlideDetail slideDetail = of.createCWSCaseScanSlideScanDetailsSetSlideDetail();
                    slideDetail.setTitle("do not update");
                    slideDetail.setText("do not update");
                    slideDetail.setType("system");
                    slideDetail.setCtrltype("Text");
                    slideDetail.setEditable("True");
                    slideDetail.setMandatory("False");
                    cws.getCase().getScanSlide().getScanDetailsSet().getSlideDetail().add(slideDetail);
                }

            }
            else {
                
                casePartNameMap.get(tsvMap.get("specnum_formatted")).add(tsvMap.get("name"));
                
            }
            
        }
        
        for(CWS cws : caseMap.values()) {
            
            LOGGER.info("writing case " + cws.getCase().getName());
            
            {
                StringBuffer partNameSb = new StringBuffer();
                for(String partName : casePartNameMap.get(cws.getCase().getName())) {
                    partNameSb.append((partNameSb.length() == 0 ? "" : "/") + partName);
                }
                CWS.Case.CaseDetailsSet.CaseDetail caseDetail = of.createCWSCaseCaseDetailsSetCaseDetail();
                caseDetail.setTitle("Specimen type");
                caseDetail.setText(partNameSb.toString());
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
