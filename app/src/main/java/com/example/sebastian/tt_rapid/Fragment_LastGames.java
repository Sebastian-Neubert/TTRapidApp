package com.example.sebastian.tt_rapid;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import android.support.v7.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Sebastian on 08.09.2015.
 */
public class Fragment_LastGames extends android.support.v4.app.Fragment {

    private RecyclerView recyclerView;
    private Adapter_RecyclerView_LastGames mAdapter;


    List<LastGames_SpielDaten> data = new ArrayList<>();

    public static Fragment_LastGames newInstance(int mannschaftsID) {
        Fragment_LastGames myFragment = new Fragment_LastGames();
        Bundle arguments = new Bundle();
        arguments.putInt("mannschaftID", mannschaftsID);
        myFragment.setArguments(arguments);
        return myFragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_frag_lastgames, container, false);

        Bundle args = getArguments();
        int mID = args.getInt("mannschaftID", 0);
        // adapter einfügen und in irgendeiner variablen speichern
        recyclerView = (RecyclerView) view.findViewById(R.id.fragment_mannschaft_spiele);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        mAdapter = new Adapter_RecyclerView_LastGames(getActivity().getApplicationContext(), data);
        recyclerView.setAdapter(mAdapter);

        String[] mannschaften = getResources().getStringArray(R.array.Mannschaften);
        for (String mannschaft : mannschaften) {
            String[] mannschaftDetail = getResources().getStringArray(getResources().getIdentifier(mannschaft, "array", getActivity().getPackageName()));
            if (Integer.parseInt(mannschaftDetail[7]) == mID) {
                new DownloadFileFromURL().execute(mannschaftDetail[3], mannschaftDetail[2]);
            }
        }
        return view;

    }



    class DownloadFileFromURL extends AsyncTask<String, String, String> {

        /**
         * Downloading file in background thread
         */
        String mannschaft;

        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                mannschaft = f_url[1];
                URL url = new URL(f_url[0]);
                mAdapter.setMannschaft(mannschaft);

                URLConnection conection = url.openConnection();
                conection.connect();

                // this will be useful so that you can show a tipical 0-100%
                // progress bar
                int lenghtOfFile = conection.getContentLength();

                // download the file
                InputStream input = new BufferedInputStream(url.openStream(),
                        8192);

                // Output stream
                OutputStream output = new FileOutputStream(Environment
                        .getExternalStorageDirectory().toString()
                        + "/tmp.xml");

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(String file_url) {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = null;
            try {
                db = dbf.newDocumentBuilder();
                Document dom = null;
                try {
                    dom = db.parse("file://" + Environment
                            .getExternalStorageDirectory().toString()
                            + "/tmp.xml");
                } catch (SAXException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Element docEle = dom.getDocumentElement();
                NodeList nl = docEle.getChildNodes();
                if (nl != null && nl.getLength() > 0) {
                    Element content = (Element) docEle.getElementsByTagName("Content").item(0);

                    for (int i = 0; i < content.getChildNodes().getLength(); i++) {
                        if (content.getChildNodes().item(i).getNodeType() == Node.ELEMENT_NODE) {
                            Element el = (Element) content.getChildNodes().item(i);
                            if (el.getNodeName().contains("Spiel")) {
                                LastGames_SpielDaten current = new LastGames_SpielDaten();
                                current.heim = el.getElementsByTagName("Heimmannschaft").item(0).getTextContent();
                                current.gast = el.getElementsByTagName("Gastmannschaft").item(0).getTextContent();

                                if (current.heim.equals(mannschaft) || current.gast.equals(mannschaft)) {

                                    Date datee = null;
                                    String date = el.getElementsByTagName("Datum").item(0).getTextContent();
                                    SimpleDateFormat sourceFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.GERMANY);                 //("yyyy-MM-ddTHH:mm:ss");
                                    SimpleDateFormat DesiredFormat = new SimpleDateFormat("dd.MM.yyyy  HH:mm", Locale.GERMANY);
                                    try {
                                        datee = sourceFormat.parse(date);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    date = DesiredFormat.format(datee.getTime());
                                    current.datum = date;

                                    current.erg = el.getElementsByTagName("Ergebnis").item(0).getTextContent();
                                    if (current.erg.equals("Vorbericht")) {
                                        current.erg = "0 : 0";
                                    }
                                    if (current.erg.equals("")){
                                        current.erg = "0 : 0";
                                    }
                                    data.add(current);
                                }
                            }
                        }
                        mAdapter.notifyDataSetChanged();

                    }


                }
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            }


        }
    }


}