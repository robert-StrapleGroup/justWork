package io;

import objects.HistoryObj;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by robert on 13.08.2015.
 */
public class XmlParser {
    File historyFile = null;
    Document history = null;
    Element root;

    /*
    *   Konstruktor parsera - otwiera plik xml z historią
    * */
    public XmlParser() {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            historyFile = new File("history.xml");
            if (historyFile.exists() && !historyFile.isDirectory()) {
                history = dBuilder.parse(historyFile);
                root = history.getDocumentElement();
            } else {
                history = dBuilder.newDocument();
                root = history.createElement("justRemember_history");
                history.appendChild(root);
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    *   tworzy nową aktywność
    * */

    public void createActivity(String aName) {
        if (!isActivityExists(aName)) {
            Element pNewActivity = history.createElement("activity");
            pNewActivity.setAttribute("name", aName);
            root.appendChild(pNewActivity);
        }
    }

    /*
    *   sprawdza istnienie podanej aktywności
    * */

    public boolean isActivityExists(String aName) {
        boolean flag = false;
        NodeList activityList = root.getElementsByTagName("activity");
        for (int i = 0; i < activityList.getLength(); i++) {
            Element pElement = (Element) activityList.item(i);
            if (pElement.getAttribute("name").toLowerCase().equals(aName.toLowerCase())) {
                flag = true;
            }
        }
        return flag;
    }

    /*
    *   Zwraca wszystkie stworzone aktywności
    * */
    public List<String> getAllActivities() {
        List<String> pActivities = new ArrayList<String>();
        NodeList activityList = root.getElementsByTagName("activity");
        for (int i = 0; i < activityList.getLength(); i++) {
            Element pElement = (Element) activityList.item(i);
            pActivities.add(pElement.getAttribute("name"));
        }
        return pActivities;
    }

    /*
    *   zwraca kompletny czas podanej aktywności
    * */
    // format hh/mm/ss
    public List<String> getCompleteTime(String activity) {
        List<String> time = new ArrayList<>();
        NodeList activityList = root.getElementsByTagName("activity");
        for (int i = 0; i < activityList.getLength(); i++) {
            Element pElement = (Element) activityList.item(i);
            if (pElement.getAttribute("name").toLowerCase().equals(activity.toLowerCase())) {
                NodeList pCompleteTime = pElement.getElementsByTagName("total");
                if (pCompleteTime.getLength() > 0) {
                    time.add(((Element) pCompleteTime.item(0)).getAttribute("h"));
                    time.add(((Element) pCompleteTime.item(0)).getAttribute("min"));
                    time.add(((Element) pCompleteTime.item(0)).getAttribute("s"));
                } else {
                    time.add("0");
                    time.add("0");
                    time.add("0");
                }
            }

        }
        return time;
    }

    /*
    *   ustawia kompletny czas dla podanej aktywności; uruchamiane pod koniec sesji
    * */

    public void setCompleteTime(String h, String min, String s, String activity) {
        NodeList activityList = root.getElementsByTagName("activity");
        for (int i = 0; i < activityList.getLength(); i++) {
            Element pElement = (Element) activityList.item(i);
            if (pElement.getAttribute("name").toLowerCase().equals(activity.toLowerCase())) {
                NodeList pCompleteTime = pElement.getElementsByTagName("total");
                if (pCompleteTime.getLength() > 0) {
                    ((Element) pCompleteTime.item(0)).setAttribute("h", h);
                    ((Element) pCompleteTime.item(0)).setAttribute("min", min);
                    ((Element) pCompleteTime.item(0)).setAttribute("s", s);

                } else {
                    Element newCompleteTime = history.createElement("total");
                    newCompleteTime.setAttribute("h", h);
                    newCompleteTime.setAttribute("s", s);
                    newCompleteTime.setAttribute("min", min);
                    pElement.appendChild(newCompleteTime);
                }
            }

        }
    }

    /*
    *   dodaje informacje o odbytej sesji do historii
    * */

    public void addHistoryItem(HistoryObj historyObj, String activity) {
        NodeList activityList = root.getElementsByTagName("activity");
        for (int i = 0; i < activityList.getLength(); i++) {
            Element pElement = (Element) activityList.item(i);
            if (pElement.getAttribute("name").toLowerCase().equals(activity.toLowerCase())) {
                if (pElement.getElementsByTagName("history").getLength() < 1) {
                    pElement.appendChild(history.createElement("history"));
                }
                Element pHistory = (Element) pElement.getElementsByTagName("history").item(0);
                Element historyItem = history.createElement("item");
                historyItem.setAttribute("id", historyObj.getId());
                historyItem.setAttribute("startDate", historyObj.getDateStart());
                historyItem.setAttribute("endDate", historyObj.getDateEnd());
                historyItem.setAttribute("timeElapsed", historyObj.getTimeElapsed());
                historyItem.setAttribute("annotations", historyObj.getAnnotations());
                pHistory.appendChild(historyItem);
            }
        }
    }

    /*
    *   zwraca całą historię podanej aktywności
    * */

    public List<HistoryObj> getAllHistory(String activity) {
        List<HistoryObj> pHistoryList = new ArrayList<HistoryObj>();
        NodeList activityList = root.getElementsByTagName("activity");
        for (int i = 0; i < activityList.getLength(); i++) {
            Element pElement = (Element) activityList.item(i);
            if (pElement.getAttribute("name").toLowerCase().equals(activity.toLowerCase())) {
                if (pElement.getElementsByTagName("history").getLength() == 1) {
                    Element pHistory = (Element) pElement.getElementsByTagName("history").item(0);
                    NodeList pHistoryItems = pHistory.getElementsByTagName("item");
                    for (int j = 0; j < pHistoryItems.getLength(); j++) {
                        Element pHistoryItem = (Element) pHistoryItems.item(j);
                        HistoryObj pHistoryObj = new HistoryObj(pHistoryItem.getAttribute("id"),
                                pHistoryItem.getAttribute("startDate"),
                                pHistoryItem.getAttribute("endDate"),
                                pHistoryItem.getAttribute("timeElapsed"),
                                pHistoryItem.getAttribute("annotations"));
                        pHistoryList.add(pHistoryObj);
                    }
                }
            }
        }
        return pHistoryList;
    }

    /*
    *   zapisuje wszystkie zmiany w strukturze xml-a
    * */

    public void saveXml() {
        TransformerFactory transformerFactory =
                TransformerFactory.newInstance();
        Transformer transformer =
                null;
        try {
            transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(history);
            StreamResult result =
                    new StreamResult(historyFile);
            transformer.transform(source, result);
            StreamResult consoleResult =
                    new StreamResult(System.out);
            transformer.transform(source, consoleResult);
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }
}
