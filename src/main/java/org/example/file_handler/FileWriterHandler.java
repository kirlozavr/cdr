package org.example.file_handler;

import org.example.entity.SubscriberCallsInformation;
import org.example.entity.SubscriberEntityReport;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;

public class FileWriterHandler {

    private static final String FILE_PATH = ".\\src\\main\\resources\\reports\\";
    private FileWriter fileWriter;

    public FileWriterHandler(String fileName) throws IOException {
        File file = new File(FILE_PATH + fileName + ".txt");
        fileWriter = new FileWriter(file);
    }

    public void writeEntityReport(SubscriberEntityReport subscriberEntityReport) throws IOException {
        fileWriter.write("Tariff index: " + subscriberEntityReport.getTariff() + "\n");
        fileWriter.write("----------------------------------------------------------------------------" + "\n");
        fileWriter.write("Report for phone number " + subscriberEntityReport.getPhoneNumber() + ":" + "\n");
        fileWriter.write("----------------------------------------------------------------------------" + "\n");
        fileWriter.write("| Call Type |   Start Time        |     End Time        | Duration | Cost  |" + "\n");
        fileWriter.write("----------------------------------------------------------------------------" + "\n");
        for(SubscriberCallsInformation information : subscriberEntityReport.getCallsInformationList()){
            fileWriter.write(
                    String.format("|     %s    | %s | %s | %s |%6s |%n",
                            information.getCallType(),
                            information.getStartTime(),
                            information.getEndTime(),
                            information.getDuration(),
                            new DecimalFormat("0.00").format(information.getCost())
                    )
            );
        }
        fileWriter.write("----------------------------------------------------------------------------" + "\n");
        fileWriter.write(
                String.format("|                                           Total Cost: |%10s rubles |%n",
                        new DecimalFormat("0.00").format(subscriberEntityReport.getTotalCost()))
        );
        fileWriter.write("----------------------------------------------------------------------------" + "\n");
        fileWriter.close();
    }
}
