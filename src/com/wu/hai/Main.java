package com.wu.hai;

import java.io.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class Main implements ActionListener {
	JFrame frame = new JFrame("激活码生成器");
	JTabbedPane tabPane = new JTabbedPane();// 选项卡布局
	Container con = new Container();// 布局1
	Container con1 = new Container();// 布局2
	JLabel label1 = new JLabel("选择源文件");
	JLabel label2 = new JLabel("新文件地址");
	JTextField text1 = new JTextField();
	JTextField text2 = new JTextField();
	JTextField text3 = new JTextField();
	JButton button1 = new JButton("...");
	JButton button2 = new JButton("run");
	JFileChooser jfc = new JFileChooser();// 文件选择器

	Main() {
		jfc.setCurrentDirectory(new File("d:\\"));// 文件选择器的初始目录定为d盘
		// 下面两行是取得屏幕的高度和宽度
		double lx = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		double ly = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		frame.setLocation(new Point((int) (lx / 2) - 150, (int) (ly / 2) - 150));// 设定窗口出现位置
		frame.setSize(600, 500);// 设定窗口大小
		frame.setContentPane(tabPane);// 设置布局
		// 下面设定标签等的出现位置和高宽
		label1.setBounds(10, 10, 70, 20);
		label2.setBounds(10, 30, 100, 20);
		text1.setBounds(80, 10, 120, 20);
		text2.setBounds(80, 30, 120, 20);
		text3.setBounds(310, 90, 90, 40);
		text3.setVisible(false);
		button1.setBounds(210, 10, 50, 20);
		button2.setBounds(310, 10, 90, 40);
		text2.setEnabled(false);// 为该文本框设置为只读
		button1.addActionListener(this);// 添加事件处理
		button2.addActionListener(this);// 添加事件处理
		con.add(label1);
		con.add(label2);
		con.add(text1);
		con.add(text2);
		con.add(text3);
		con.add(button1);
		con.add(button2);
		con.add(jfc);
		tabPane.add("文件选择", con);// 添加布局1
		// tabPane.add("暂无内容",con1);//添加布局2
		frame.setVisible(true);// 窗口可见
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// 使能关闭窗口，结束程序
	}

	public void actionPerformed(ActionEvent e) {// 事件处理
		if (e.getSource().equals(button1)) {
			text3.setVisible(false);
			jfc.setFileSelectionMode(0);// 设定只能选择到文件
			int state = jfc.showOpenDialog(null);// 此句是打开文件选择器界面的触发语句
			if (state == 1) {
				return;// 撤销则返回
			} else {
				File f = jfc.getSelectedFile();// f为选择到的文件
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
			File file = new File(str1);// 读取的文本
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
			} // 构造一个BufferedReader类来读取文件
			String s = null;
			try {
				while ((s = br.readLine()) != null) {// 使用readLine方法，一次读一行
					String mac = s.trim().toUpperCase();// 获取每行的值并去除前后的空格并转换成大写
					if (mac.substring(2, 3).equals(":") || mac.substring(2, 3).equals("-")) {
						//当mac地址有":"或者"-"时
						SyncTask job = new SyncTask();
						String my = job.sync(mac);
						String my3 = my.substring(0, 8).toLowerCase();
						writer.println(s + "        " + my3);
					} else {
						if (mac.substring(0, 2).equals("06")) {
							// 每两位添加一个冒号
							String regex = "(.{2})";
							String newMac = mac.replaceAll(regex, "$1:");
							String my2 = newMac.substring(0, 17);
							SyncTask job = new SyncTask();
							String my = job.sync(my2);
							String my3 = my.substring(0, 8).toLowerCase();
							writer.println(s + "        " + my3);
						}
						if (mac.substring(0, 2).equals("04")) {
							// 每两位添加一个冒号
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
			File file2 = new File(str2);// 读取的文本
			if (file2.length() > 0) {
				text3.setVisible(true);
				text3.setText("已完成！");
			} else {
				text3.setVisible(true);
				text3.setText("未完成！");
			}
		}
	}

	public static void main(String[] args) {
		new Main();

	}
}