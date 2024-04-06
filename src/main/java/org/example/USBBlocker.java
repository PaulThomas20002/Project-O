package org.example;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class USBBlocker {

    private static JButton startButton;
    private static JButton endButton;

    public static void main(String[] args) {
        // Create Swing frame
        JFrame frame = new JFrame("USB Blocker");
        frame.setSize(300, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create panel to hold buttons
        JPanel panel = new JPanel();
        frame.add(panel);

        // Create start button
        startButton = new JButton("Start Blocking");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startBlocking();
            }
        });
        panel.add(startButton);

        // Create end button
        endButton = new JButton("End Blocking");
        endButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                endBlocking();
            }
        });
        panel.add(endButton);

        // Set frame visibility
        frame.setVisible(true);
    }

    private static void startBlocking() {
        startButton.setEnabled(false);
        endButton.setEnabled(true);
        // Start blocking process
        blockUSBStorageDevices();
    }

    private static void endBlocking() {
        startButton.setEnabled(true);
        endButton.setEnabled(false);
        // End blocking process
        // You may want to add code here to restore USB access
    }

    public static void blockUSBStorageDevices() {
        try {
            // Step 1: Identify USB storage devices
            Process listProcess = Runtime.getRuntime().exec("lsblk -o NAME,TYPE");
            BufferedReader reader = new BufferedReader(new InputStreamReader(listProcess.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                // Check if the device is a USB storage device
                if (line.contains("disk")) {
                    String[] parts = line.split("\\s+");
                    String deviceName = parts[0];

                    // Step 2: Unmount the USB storage device
                    Process unmountProcess = Runtime.getRuntime().exec("sudo umount /dev/" + deviceName);
                    unmountProcess.waitFor();

                    // Step 3: (Optional) Modify permissions or restrict access to the device nodes
                    // This may require additional Linux commands or udev rules

                    System.out.println("USB storage device " + deviceName + " blocked.");
                }
            }
            reader.close();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}