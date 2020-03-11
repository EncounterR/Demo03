package com.wu.hai;

import java.io.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class Main implements ActionListener {
	JFrame frame = new JFrame("������������");
	JTabbedPane tabPane = new JTabbedPane();// ѡ�����
	Container con = new Container();// ����1
	Container con1 = new Container();// ����2
	JLabel label1 = new JLabel("ѡ��Դ�ļ�");
	JLabel label2 = new JLabel("���ļ���ַ");
	JTextField text1 = new JTextField();
	JTextField text2 = new JTextField();
	JTextField text3 = new JTextField();
	JButton button1 = new JButton("...");
	JButton button2 = new JButton("run");
	JFileChooser jfc = new JFileChooser();// �ļ�ѡ����

	Main() {
		jfc.setCurrentDirectory(new File("d:\\"));// �ļ�ѡ�����ĳ�ʼĿ¼��Ϊd��
		// ����������ȡ����Ļ�ĸ߶ȺͿ��
		double lx = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		double ly = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		frame.setLocation(new Point((int) (lx / 2) - 150, (int) (ly / 2) - 150));// �趨���ڳ���λ��
		frame.setSize(600, 500);// �趨���ڴ�С
		frame.setContentPane(tabPane);// ���ò���
		// �����趨��ǩ�ȵĳ���λ�ú͸߿�
		label1.setBounds(10, 10, 70, 20);
		label2.setBounds(10, 30, 100, 20);
		text1.setBounds(80, 10, 120, 20);
		text2.setBounds(80, 30, 120, 20);
		text3.setBounds(310, 90, 90, 40);
		text3.setVisible(false);
		button1.setBounds(210, 10, 50, 20);
		button2.setBounds(310, 10, 90, 40);
		text2.setEnabled(false);// Ϊ���ı�������Ϊֻ��
		button1.addActionListener(this);// ����¼�����
		button2.addActionListener(this);// ����¼�����
		con.add(label1);
		con.add(label2);
		con.add(text1);
		con.add(text2);
		con.add(text3);
		con.add(button1);
		con.add(button2);
		con.add(jfc);
		tabPane.add("�ļ�ѡ��", con);// ��Ӳ���1
		// tabPane.add("��������",con1);//��Ӳ���2
		frame.setVisible(true);// ���ڿɼ�
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// ʹ�ܹرմ��ڣ���������
	}

	public void actionPerformed(ActionEvent e) {// �¼�����
		if (e.getSource().equals(button1)) {
			text3.setVisible(false);
			jfc.setFileSelectionMode(0);// �趨ֻ��ѡ���ļ�
			int state = jfc.showOpenDialog(null);// �˾��Ǵ��ļ�ѡ��������Ĵ������
			if (state == 1) {
				return;// �����򷵻�
			} else {
				File f = jfc.getSelectedFile();// fΪѡ�񵽵��ļ�
				text1.setText(f.getAbsolutePath());
				int length = f.getAbsolutePath().length();
				StringBuilder path1 = new StringBuilder(f.getAbsolutePath());
				String text2FilePath = path1.insert(length - 4, "_new").toString();
				text2.setText(text2FilePath);
			}
		}

		if (e.getSource().equals(button2) && text1.getText().length() != 0) {
			String str1 = text1.getText();
			String str2 = text2.getText();
			PrintWriter writer = null;
			File file = new File(str1);// ��ȡ���ı�
			try {
				writer = new PrintWriter(str2);
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
			BufferedReader br = null;
			try {
				br = new BufferedReader(new FileReader(file));
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} // ����һ��BufferedReader������ȡ�ļ�
			String s = null;
			try {
				while ((s = br.readLine()) != null) {// ʹ��readLine������һ�ζ�һ��
					String mac = s.trim().toUpperCase();// ��ȡÿ�е�ֵ��ȥ��ǰ��Ŀո�ת���ɴ�д
					if (mac.substring(2, 3).equals(":") || mac.substring(2, 3).equals("-")) {
						//��mac��ַ��":"����"-"ʱ
						SyncTask job = new SyncTask();
						String my = job.sync(mac);
						String my3 = my.substring(0, 8).toLowerCase();
						writer.println(s + "        " + my3);
					} else {
						if (mac.substring(0, 2).equals("06")) {
							// ÿ��λ���һ��ð��
							String regex = "(.{2})";
							String newMac = mac.replaceAll(regex, "$1:");
							String my2 = newMac.substring(0, 17);
							SyncTask job = new SyncTask();
							String my = job.sync(my2);
							String my3 = my.substring(0, 8).toLowerCase();
							writer.println(s + "        " + my3);
						}
						if (mac.substring(0, 2).equals("04")) {
							// ÿ��λ���һ��ð��
							String regex = "(.{2})";
							String newMac = mac.replaceAll(regex, "$1-");
							String my2 = newMac.substring(0, 17);
							SyncTask job = new SyncTask();
							String my = job.sync(my2);
							String my3 = my.substring(0, 8).toLowerCase();
							writer.println(s + "        " + my3);
						}
					}

				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			try {
				br.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			writer.close();
			File file2 = new File(str2);// ��ȡ���ı�
			if (file2.length() > 0) {
				text3.setVisible(true);
				text3.setText("����ɣ�");
			} else {
				text3.setVisible(true);
				text3.setText("δ��ɣ�");
			}
		}
	}

	public static void main(String[] args) {
		new Main();

	}
}