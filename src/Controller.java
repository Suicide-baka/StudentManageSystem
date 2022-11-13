package studentManageSystem.com.zse.controller;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import com.mysql.jdbc.PreparedStatement;
import studentManageSystem.com.zse.dao.User;
import studentManageSystem.com.zse.util.DbUtil;
import studentManageSystem.com.zse.util.StringUtil;
import studentManageSystem.com.zse.view.AdminFrame;
import studentManageSystem.com.zse.view.LoginFrame;
import studentManageSystem.com.zse.view.UserFrame;

public class Controller {
	public static LoginFrame loginFrame;
	JPanel jpanel;
    String[] newvalue;
	String[] oldScore;
		
	public static void main(String[] args) {
		loginFrame = new LoginFrame(new Controller());
	}	
	public User login(Connection connection,User user,String role) throws SQLException{//登录
		User resultUser = null;
		String sql = null;
		if(role.equals("admin")){
			sql = "select * from administrator where username='"+user.getUsername()+"' and password='"+user.getPassword()+"'";
		}else{
			sql = "select * from user where username='"+user.getUsername()+"' and password='"+user.getPassword()+"'";
		}
		ResultSet result = statementQuery(connection, sql);
		if(result.next()){
			resultUser = new User();
			resultUser.setUsername(result.getString("username"));
			resultUser.setPassword(result.getString("password"));
			if(role.equals("admin")){
				resultUser.setSno(null);
			}else{
				resultUser.setSno(result.getString("sno"));
			}
		}
		return resultUser;
	}
	private void checkStudentInfo(Connection connection,AdminFrame adminFrame) throws SQLException{//查看学生信息
		jpanel = new JPanel();
		Object[][] object = null;
		int rowCount = 0;
		int columnCount;
		ResultSet result = null;
		String[] columnNames =  { "学号", "姓名", "性别", "系号", "专业号","出生日期"};
		
		try{
			String sql = "select sno,sname,ssex,dno,spno,sbirthday from studentinfo";
			result = statementQuery(connection, sql);
			//获取行数 
			String countSQL = "select count(*) totalCount from studentinfo";
			ResultSet rset = statementQuery(connection, countSQL);
			if(rset.next()) { 
				rowCount=rset .getInt("totalCount"); 
			} 
			//获取列数
			columnCount = result.getMetaData().getColumnCount();
			object = new Object[rowCount][columnCount];
			
			int i=0, j=0;
			while(result.next()){
				if(result.getString(1) != null){
					for(j = 0;j<6;j++){
						object[i][j]=result.getString(j+1);
					}
					i++;
				}
			}
		}catch (SQLException e) { 
			   e.printStackTrace();
		}
		JTable checkTab = new JTable(object,columnNames);
		checkTab.setEnabled(false);
		JScrollPane scrollpane = new JScrollPane(checkTab);
		
		scrollpane.setBounds(20,50,500,200);
		jpanel.setLayout(null);
		jpanel.add(scrollpane);
		
		JLabel tableTitle = new JLabel();
		tableTitle.setText("学生信息");
		tableTitle.setForeground(Color.BLUE);
		tableTitle.setHorizontalAlignment(SwingConstants.CENTER);
		tableTitle.setBounds(200, 20, 150, 20);
		jpanel.add(tableTitle);
		jpanel.setVisible(true);
		
		adminFrame.add(jpanel);
		adminFrame.setSize(550,350);
		adminFrame.setResizable(false);
	}
	private void addStudentInfo(final Connection connection,AdminFrame adminFrame){//添加学生信息
		jpanel = new JPanel();
		JPanel leftJpanel,rightJpanel,titleJpanel,btnJpanel;
		JButton addBtn;
		final JButton resetBtn;
		JLabel addLabel;
		final JTextField sno,sname,sbirthday;
		final JComboBox<String>  ssex;
		final JComboBox<String> dnoBox;
		final JComboBox<String> spnoBox;
		
		leftJpanel = new JPanel(new GridLayout(6,1));
		rightJpanel = new JPanel(new GridLayout(6,1));
		titleJpanel = new JPanel();
		btnJpanel = new JPanel();

		addBtn = new JButton("添加");
		resetBtn = new JButton("重置");
		
		addLabel = new JLabel("添加学生信息",SwingConstants.CENTER);
		
		sno = new JTextField(20);
		sname = new JTextField(20);
		ssex = new JComboBox<String>();
		dnoBox =  new JComboBox<String>();
		spnoBox =new JComboBox<String>();
		sbirthday = new JTextField(20);
		sbirthday.setText("格式如：2000/01/01");
		
		ssex.addItem("男");
		ssex.addItem("女");
		
		ssex.setSelectedItem("男");
		try {
			String dnoSQL = "select dno from department order by dno";
			ResultSet dnoItem = statementQuery(connection, dnoSQL);
			while(dnoItem.next()){
				dnoBox.addItem(dnoItem.getString(1));
			}
		} catch (SQLException e2) {
			e2.printStackTrace();
		} 
		createSpnoItem(connection, spnoBox, (String)dnoBox.getSelectedItem());
		
		dnoBox.setSelectedIndex(0);
		leftJpanel.add(new JLabel("学号",SwingConstants.CENTER));
		leftJpanel.add(new JLabel("姓名",SwingConstants.CENTER));
		leftJpanel.add(new JLabel("性别",SwingConstants.CENTER));
		leftJpanel.add(new JLabel("系号",SwingConstants.CENTER));
		leftJpanel.add(new JLabel("专业号",SwingConstants.CENTER));
		leftJpanel.add(new JLabel("出生日期",SwingConstants.CENTER));
		
		rightJpanel.add(sno);
		rightJpanel.add(sname);
		rightJpanel.add(ssex);
		rightJpanel.add(dnoBox);
		rightJpanel.add(spnoBox);
		rightJpanel.add(sbirthday);
		
		titleJpanel.add(addLabel);
		btnJpanel.add(addBtn);
		btnJpanel.add(resetBtn);
		
		titleJpanel.setBounds(170,0,200,45);
		leftJpanel.setBounds(20,45,100,200);
		rightJpanel.setBounds(150,45,300,200);
		btnJpanel.setBounds(170,270,200,50);
		jpanel.add(titleJpanel);
		jpanel.add(leftJpanel);
		jpanel.add(rightJpanel);
		jpanel.add(btnJpanel);
		jpanel.setLayout(null);
		adminFrame.add(jpanel);
		adminFrame.setVisible(true);
		adminFrame.setSize(550,400);
		//文本框加入提示语
		sbirthday.addFocusListener(new FocusListener(){
	    	@Override
			public void focusGained(FocusEvent e) {
	    		sbirthday.setText("");
	    	}
	    	@Override
			public void focusLost(FocusEvent e) {
	    		if(StringUtil.isEmpty(sbirthday.getText())){
	    			sbirthday.setText("格式如：2000/01/01");
	    		}
	    	}
	    });
		addBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String	sql = "insert into studentinfo (sno,sname,ssex,dno,spno,sbirthday) values ('"+sno.getText()+"','"+sname.getText()+"','"+ssex.getSelectedItem()+"','"+dnoBox.getSelectedItem()+"','"+spnoBox.getSelectedItem()+"','"+sbirthday.getText()+"')";
				if(StringUtil.isEmpty(sno.getText())){		
					JOptionPane.showMessageDialog(null, "学号不能为空");
					return ;
				}
				if(StringUtil.isEmpty(sname.getText())){
					JOptionPane.showMessageDialog(null, "姓名不能为空");
					return ;
				}
				try {
					int result = statementUpdate(connection, sql);
					if(result!=0){
						JOptionPane.showMessageDialog(null, "插入成功");
						resetBtn.doClick();
					}
				} catch (Exception e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog(null, "添加失败");
				}
			}
		});
		
		resetBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sno.setText("");
				sname.setText("");
				ssex.setSelectedItem("男");
				dnoBox.setSelectedIndex(0);
				spnoBox.setSelectedIndex(0);
				sbirthday.setText("");
			}
		});
		dnoBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String dno = (String) dnoBox.getSelectedItem();	
				createSpnoItem(connection, spnoBox, dno);
			}
		}); 
	}
	private void modifyStudentInfo(final Connection connection,AdminFrame adminFrame){//修改学生信息
		jpanel = new JPanel();
		JPanel leftJpanel,rightJpanel,titleJpanel,btnJpanel;
		JButton searchSno;
		final JButton editBtn;
		final JButton modifyBtn;
		final JButton deleteBtn;
		JLabel addLabel;
		final JTextField sno,sname,sbirthday;
		final JComboBox<String>  ssex;
		final JComboBox<String> dnoBox;
		final JComboBox<String> spnoBox; 
		final String[] snoText=new String[1];
		
		leftJpanel = new JPanel(new GridLayout(6,1));
		rightJpanel = new JPanel(new GridLayout(6,1));
		titleJpanel = new JPanel();
		btnJpanel = new JPanel();
		editBtn = new JButton("编辑");
		modifyBtn = new JButton("修改");
		deleteBtn = new JButton("删除");
		searchSno = new JButton("搜索");
		
		addLabel = new JLabel("修改学生信息",SwingConstants.CENTER);
		
		sno = new JTextField(20);
		sname = new JTextField(20);
		ssex = new JComboBox<String>();
		dnoBox =  new JComboBox<String>();
		spnoBox =new JComboBox<String>();
		sbirthday = new JTextField(20);
		sbirthday.setText("格式如：2000/01/01");
		
		ssex.addItem("男");
		ssex.addItem("女");
		
		ssex.setSelectedItem("男");
		try {
			String dnoSQL = "select dno from department order by dno";
			ResultSet dnoItem = statementQuery(connection, dnoSQL);
			while(dnoItem.next()){
				dnoBox.addItem(dnoItem.getString(1));
			}
		} catch (SQLException e2) {
			e2.printStackTrace();
		} 
		createSpnoItem(connection, spnoBox, (String)dnoBox.getSelectedItem());
		
		dnoBox.setSelectedIndex(0);
		leftJpanel.add(new JLabel("输入修改的学号",SwingConstants.CENTER));
		leftJpanel.add(new JLabel("姓名",SwingConstants.CENTER));
		leftJpanel.add(new JLabel("性别",SwingConstants.CENTER));
		leftJpanel.add(new JLabel("系号",SwingConstants.CENTER));
		leftJpanel.add(new JLabel("专业号",SwingConstants.CENTER));
		leftJpanel.add(new JLabel("出生日期",SwingConstants.CENTER));
		
		rightJpanel.add(sno);
		rightJpanel.add(sname);
		rightJpanel.add(ssex);
		rightJpanel.add(dnoBox);
		rightJpanel.add(spnoBox);
		rightJpanel.add(sbirthday);
		
		sname.setEditable(false);
		ssex.setEnabled(false);
		dnoBox.setEnabled(false);
		spnoBox.setEnabled(false);
		sbirthday.setEditable(false);
		editBtn.setEnabled(false);
		modifyBtn.setEnabled(false);
		deleteBtn.setEnabled(false);
		
		titleJpanel.add(addLabel);
		btnJpanel.add(editBtn);
		btnJpanel.add(modifyBtn);
		btnJpanel.add(deleteBtn);
		
		searchSno.setBounds(460, 53, 60, 23);
		titleJpanel.setBounds(170,0,200,45);
		leftJpanel.setBounds(20,45,100,200);
		rightJpanel.setBounds(150,45,300,200);
		btnJpanel.setBounds(170,270,200,50);
		jpanel.add(searchSno);
		jpanel.add(titleJpanel);
		jpanel.add(leftJpanel);
		jpanel.add(rightJpanel);
		jpanel.add(btnJpanel);
		jpanel.setLayout(null);
		adminFrame.add(jpanel);
		adminFrame.setVisible(true);
		adminFrame.setSize(550,400);
		//文本框加入提示语
		sbirthday.addFocusListener(new FocusListener(){
	    	@Override
			public void focusLost(FocusEvent e) {
	    		if(StringUtil.isEmpty(sbirthday.getText())){
	    			sbirthday.setText("格式如：2000/01/01");
	    		}
	    	}
			@Override
			public void focusGained(FocusEvent e) {}
	    });
		searchSno.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				snoText[0] = sno.getText();
				if(snoText[0]!= null){
					String searchSql = "select * from studentinfo where sno = '"+sno.getText()+"'";
					try {
						ResultSet result = statementQuery(connection, searchSql);
						if(result.next()){
							sno.setText(result.getString("sno"));
							sname.setText(result.getString("sname"));
							ssex.setSelectedItem(result.getString("ssex"));
							dnoBox.setSelectedItem(result.getString("dno"));
							spnoBox.setSelectedItem(result.getString("spno"));
							sbirthday.setText(result.getString("sbirthday"));
							deleteBtn.setEnabled(true);
							editBtn.setEnabled(true);
						}else{
							JOptionPane.showMessageDialog(null, "不存在该学号");
						}
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				}else{
					JOptionPane.showMessageDialog(null, "请输入需要修改的学号");
				}
			}
		});
		editBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sname.setEditable(true);
				ssex.setEnabled(true);
				dnoBox.setEnabled(true);
				spnoBox.setEnabled(true);
				sbirthday.setEditable(true);
				modifyBtn.setEnabled(true);
			}
		});
		modifyBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String updateSql;
				if(StringUtil.isEmpty(sno.getText())){
					JOptionPane.showMessageDialog(null, "学号不能为空");
					return ;
				}
				if(StringUtil.isEmpty(sname.getText())){
					JOptionPane.showMessageDialog(null, "姓名不能为空");
					return ;
				}
				updateSql = "update studentinfo set sno='"+sno.getText()+"', sname = '"+sname.getText()+"' ,ssex = '"+ssex.getSelectedItem().toString()+"',dno='"+dnoBox.getSelectedItem().toString()+"',spno='"+spnoBox.getSelectedItem().toString()+"',sbirthday='"+sbirthday.getText()+"' where sno='"+snoText[0]+"'";
				int result = statementUpdate(connection, updateSql);
				if(result!=0){
					JOptionPane.showMessageDialog(null, "修改成功");
				}
			}
		});
		
		deleteBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String deleteSql = "delete from studentinfo where sno = '"+sno.getText()+"'";
				int result = statementUpdate(connection, deleteSql);
				if(result!=0){
					JOptionPane.showMessageDialog(null, "删除成功");
					sno.setText("");
					sname.setText("");
					ssex.setSelectedItem("男");
					dnoBox.setSelectedIndex(0);
					spnoBox.setSelectedIndex(0);
					sbirthday.setText("");
					sname.setEditable(false);
					ssex.setEnabled(false);
					dnoBox.setEnabled(false);
					spnoBox.setEnabled(false);
					sbirthday.setEditable(false);
					editBtn.setEnabled(false);
					modifyBtn.setEnabled(false);
					deleteBtn.setEnabled(false);
				}else{
					JOptionPane.showMessageDialog(null, "删除失败");
				}
			}
		});
		dnoBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String dno = (String) dnoBox.getSelectedItem();	
				createSpnoItem(connection, spnoBox, dno);
			}
		}); 
	}
	private void addCourse(final Connection connection,AdminFrame adminFrame){//添加课程
		jpanel = new JPanel();
		JPanel leftJpanel,rightJpanel,titleJpanel,btnJpanel;
		JButton addBtn;
		final JButton resetBtn;
		JLabel addLabel;
		final JTextField cno,cname,number;
		final JComboBox<String>  semester;
		final JComboBox<String> credit;
		
		leftJpanel = new JPanel(new GridLayout(6,1));
		rightJpanel = new JPanel(new GridLayout(6,1));
		titleJpanel = new JPanel();
		btnJpanel = new JPanel();

		addBtn = new JButton("添加");
		resetBtn = new JButton("重置");
		
		addLabel = new JLabel("添加课程",SwingConstants.CENTER);
		
		cno = new JTextField(20);
		cname = new JTextField(20);
		semester = new JComboBox<String>();
		credit =  new JComboBox<String>();
		number = new JTextField(20);
		cno.setText("格式:c01");
		
		semester.addItem("1");
		semester.addItem("2");
		semester.addItem("3");
		semester.addItem("4");
		semester.setSelectedIndex(0);
		
		credit.addItem("0");
		credit.addItem("1");
		credit.addItem("2");
		credit.addItem("3");
		credit.addItem("4");
		credit.addItem("5");
		credit.setSelectedIndex(0);
		
		leftJpanel.add(new JLabel("课程号",SwingConstants.CENTER));
		leftJpanel.add(new JLabel("课程名",SwingConstants.CENTER));
		leftJpanel.add(new JLabel("开课学期",SwingConstants.CENTER));
		leftJpanel.add(new JLabel("学分",SwingConstants.CENTER));
		leftJpanel.add(new JLabel("选课人数",SwingConstants.CENTER));
		
		rightJpanel.add(cno);
		rightJpanel.add(cname);
		rightJpanel.add(semester);
		rightJpanel.add(credit);
		rightJpanel.add(number);
		
		titleJpanel.add(addLabel);
		btnJpanel.add(addBtn);
		btnJpanel.add(resetBtn);
		
		titleJpanel.setBounds(170,0,200,45);
		leftJpanel.setBounds(20,45,100,200);
		rightJpanel.setBounds(150,45,300,200);
		btnJpanel.setBounds(170,270,200,50);
		jpanel.add(titleJpanel);
		jpanel.add(leftJpanel);
		jpanel.add(rightJpanel);
		jpanel.add(btnJpanel);
		jpanel.setLayout(null);
		adminFrame.add(jpanel);
		adminFrame.setVisible(true);
		adminFrame.setSize(550,400);
				//文本框加入提示语
		cno.addFocusListener(new FocusListener(){
	    	@Override
			public void focusGained(FocusEvent e) {
	    		cno.setText("");
	    	}
	    	@Override
			public void focusLost(FocusEvent e) {
	    		if(StringUtil.isEmpty(cno.getText())){
	    			cno.setText("格式：c01");
	    		}
	    	}
	    });
		addBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String	sql = "insert into course (cno,cname,semester,credit,number) values ('"+cno.getText()+"','"+cname.getText()+"',"+semester.getSelectedItem().toString()+","+credit.getSelectedItem().toString()+","+number.getText()+")";
				if(StringUtil.isEmpty(cno.getText())){
					JOptionPane.showMessageDialog(null, "课程号不能为空");
					return ;
				}
				if(StringUtil.isEmpty(cname.getText())){
					JOptionPane.showMessageDialog(null, "课程名不能为空");
					return ;
				}
				try {
					int result = statementUpdate(connection, sql);
					if(result!=0){
						JOptionPane.showMessageDialog(null, "插入课程成功");
						resetBtn.doClick();
					}
				} catch (Exception e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog(null, "添加课程失败");
				}
			}
		});
		
		resetBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cno.setText("");
				cname.setText("");
				semester.setSelectedItem("男");
				credit.setSelectedIndex(0);
				number.setText("");
			}
		});
	}
	private void modifyCourse(final Connection connection,AdminFrame adminFrame){//修改/删除课程
		jpanel = new JPanel();
		JPanel leftJpanel,rightJpanel,titleJpanel,btnJpanel;
		JButton searchCname;
		final JButton editBtn, modifyBtn,deleteBtn;
		JLabel addLabel;
		final JTextField cno,cname,number;
		final JComboBox<String>  semester;
		final JComboBox<String> credit;
		
		leftJpanel = new JPanel(new GridLayout(6,1));
		rightJpanel = new JPanel(new GridLayout(6,1));
		titleJpanel = new JPanel();
		btnJpanel = new JPanel();

		editBtn = new JButton("编辑");
		modifyBtn = new JButton("修改");
		deleteBtn = new JButton("删除");
		searchCname = new JButton("搜索");
		
		addLabel = new JLabel("修改/删除课程",SwingConstants.CENTER);
		
		cno = new JTextField(20);
		cname = new JTextField(20);
		semester = new JComboBox<String>();
		credit =  new JComboBox<String>();
		number = new JTextField(20);
		cno.setText("格式:c01");
		
		semester.addItem("1");
		semester.addItem("2");
		semester.addItem("3");
		semester.addItem("4");
		semester.setSelectedIndex(0);
		
		credit.addItem("0");
		credit.addItem("1");
		credit.addItem("2");
		credit.addItem("3");
		credit.addItem("4");
		credit.addItem("5");
		credit.setSelectedIndex(0);
		
		leftJpanel.add(new JLabel("课程名",SwingConstants.CENTER));
		leftJpanel.add(new JLabel("课程号",SwingConstants.CENTER));
		leftJpanel.add(new JLabel("开课学期",SwingConstants.CENTER));
		leftJpanel.add(new JLabel("学分",SwingConstants.CENTER));
		leftJpanel.add(new JLabel("选课人数",SwingConstants.CENTER));
		
		rightJpanel.add(cname);
		rightJpanel.add(cno);
		rightJpanel.add(semester);
		rightJpanel.add(credit);
		rightJpanel.add(number);
		
		titleJpanel.add(addLabel);
		btnJpanel.add(editBtn);
		btnJpanel.add(modifyBtn);
		btnJpanel.add(deleteBtn);
		
		titleJpanel.setBounds(170,0,200,45);
		leftJpanel.setBounds(20,45,100,200);
		rightJpanel.setBounds(150,45,300,200);
		btnJpanel.setBounds(170,270,200,50);
		searchCname.setBounds(460, 53, 60, 23);
		jpanel.add(searchCname);
		jpanel.add(titleJpanel);
		jpanel.add(leftJpanel);
		jpanel.add(rightJpanel);
		jpanel.add(btnJpanel);
		jpanel.setLayout(null);
		adminFrame.add(jpanel);
		adminFrame.setVisible(true);
		adminFrame.setSize(550,400);
		
		cno.setEnabled(false);
		semester.setEnabled(false);
		credit.setEnabled(false);
		number.setEnabled(false);
		modifyBtn.setEnabled(false);
		deleteBtn.setEnabled(false);
		final String[] cnameText = new String[1];
		searchCname.addActionListener(new ActionListener() {
			@SuppressWarnings("unused")
			@Override
			public void actionPerformed(ActionEvent e) {
				cnameText[0] = cname.getText();
				if(cnameText!= null){
					String searchSql = "select * from course where cname = '"+ cnameText[0]+"'";
					try {
						ResultSet result = statementQuery(connection, searchSql);
						if(result.next()){
							cno.setText(result.getString("cno"));
							semester.setSelectedItem(result.getString("semester"));
							credit.setSelectedItem(result.getString("credit"));
							number.setText(result.getString("number"));
							deleteBtn.setEnabled(true);
						}else{
							JOptionPane.showMessageDialog(null, "不存在该课程");
						}
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				}else{
					JOptionPane.showMessageDialog(null, "请输入需要修改的课程名");
				}
			}
		});
		editBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cno.setEnabled(true);
				semester.setEnabled(true);
				credit.setEnabled(true);
				number.setEnabled(true);
				modifyBtn.setEnabled(true);
				deleteBtn.setEnabled(true);
			}
		});
		modifyBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String updateSql;
				
				if(StringUtil.isEmpty(cno.getText())){
					JOptionPane.showMessageDialog(null, "课程名不能为空");
					return ;
				}
				if(StringUtil.isEmpty(cname.getText())){
					JOptionPane.showMessageDialog(null, "课程名不能为空");
					return ;
				}
				updateSql = "update course set cno='"+cno.getText() +"', cname = '"+ cname.getText() +"' ,semester = "+semester.getSelectedItem().toString()+",number="+number.getText()+",credit="+credit.getSelectedItem().toString() +" where cname = '"+ cnameText[0]+"'";
				int result = statementUpdate(connection, updateSql);
				if(result!=0){
					JOptionPane.showMessageDialog(null, "修改成功");
				}
			}
		});
		deleteBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String deleteSql = "delete from course where cname = '"+ cname.getText() +"'";
				int result = statementUpdate(connection, deleteSql);
				if(result!=0){
					JOptionPane.showMessageDialog(null, "删除成功");
					cname.setText("");
					cno.setText("");
					semester.setSelectedIndex(0);
					credit.setSelectedIndex(0);
					number.setText("");
					
					cno.setEnabled(false);
					semester.setEnabled(false);
					credit.setEnabled(false);
					number.setEditable(false);
					modifyBtn.setEnabled(false);
					deleteBtn.setEnabled(false);
				}else{
					JOptionPane.showMessageDialog(null, "删除失败");
				}
			}
		});
	}
	private void editScore(final Connection connection,final AdminFrame adminFrame){//修改学生成绩
		jpanel = new JPanel();
		JPanel titleJpanel,btnJpanel,searchPanel;
		final JPanel tablePanel;
		final JButton searchBtn;
		JLabel addLabel,snameLabel;
		final JTextField cname;
		
		final String[] cnameText=new String[1];
		final JTable checkTab = new JTable();
		final int[] rowCount= new int[1];
		titleJpanel = new JPanel();
		searchPanel = new JPanel();
		btnJpanel = new JPanel();
		tablePanel = new JPanel();
		searchBtn = new JButton("搜索");
		
		addLabel = new JLabel("修改学生成绩",SwingConstants.CENTER);
		snameLabel = new JLabel("课程名",SwingConstants.CENTER);
		cname = new JTextField();
		cname.setColumns(10);
		
		titleJpanel.add(addLabel);
		
		titleJpanel.setBounds(170,0,200,45);
		searchPanel.setBounds(0, 45, 524, 50);
		tablePanel.setBounds(0, 94, 524, 175);
		btnJpanel.setBounds(170,290,200,33);
		
		jpanel.add(titleJpanel);
		jpanel.add(btnJpanel);
		jpanel.setLayout(null);
		jpanel.add(searchPanel);
		jpanel.add(tablePanel);
		
		adminFrame.add(jpanel);
		adminFrame.setResizable(false);
		snameLabel.setHorizontalAlignment(SwingConstants.CENTER);
		
		GroupLayout gl_searchPanel = new GroupLayout(searchPanel);
		gl_searchPanel.setHorizontalGroup(
			gl_searchPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_searchPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(snameLabel, GroupLayout.PREFERRED_SIZE, 112, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(cname, GroupLayout.PREFERRED_SIZE, 264, GroupLayout.PREFERRED_SIZE)
					.addGap(30)
					.addComponent(searchBtn, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE)
					.addGap(26))
		);
		gl_searchPanel.setVerticalGroup(
			gl_searchPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_searchPanel.createSequentialGroup()
					.addGap(11)
					.addGroup(gl_searchPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(snameLabel, GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE)
						.addComponent(cname, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
						.addComponent(searchBtn))
					.addContainerGap())
		);
		searchPanel.setLayout(gl_searchPanel);
		
		adminFrame.setVisible(true);
		adminFrame.setSize(550,400);
		//文本框加入提示语
		searchBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cnameText[0] = cname.getText();
				rowCount[0] = 0;
				ResultSet result = null;
				Object[][] object = null;
				String[] columnNames =  { "课程号", "学生学号", "成绩"};
				if(cnameText[0]!= null){
					String searchSql = "select student_course.cno,sno,score from student_course,course where student_course.cno = course.cno and cname = '"+ cnameText[0] +"'";
					try {
						result = statementQuery(connection, searchSql);
						//获取行数
						String countSql = "select count(*) totalCount from student_course,course where student_course.cno = course.cno and cname = '"+cname.getText()+"'";
						ResultSet rset = statementQuery(connection, countSql);
						if(rset.next()) { 
							rowCount[0]=rset.getInt("totalCount"); 
						} 
						
						newvalue = new String[rowCount[0]];
						oldScore = new String[rowCount[0]];
						
						object = new Object[rowCount[0]][3];
						int i=0, j=0;
						while(result.next()){
							if(result.getString(1) != null){
								for(j = 0;j<3;j++){
									object[i][j]=result.getString(j+1);
								}
								i++;
							}
						}
					}catch (SQLException e1) { 
						   e1.printStackTrace();
					}
					@SuppressWarnings("serial")
					DefaultTableModel dtm = new DefaultTableModel(object,columnNames) {
						@Override
						public boolean isCellEditable(int row, int column) {
							if (column == 2) {
								return true;
							} else {
								return false;
							}
						}};
						dtm.addTableModelListener(new TableModelListener() {//修改过的单元格
							@Override
							public void tableChanged(TableModelEvent e) {
								if(e.getType() == TableModelEvent.UPDATE){
							       newvalue[e.getLastRow()] =checkTab.getValueAt(e.getLastRow(),e.getColumn()).toString();
							        String updateSql = "update student_course set score = "+ checkTab.getValueAt(e.getLastRow(),e.getColumn()).toString()+" where sno = '"+ checkTab.getValueAt(e.getLastRow(),1).toString()+"' and cno = '"+ checkTab.getValueAt(e.getLastRow(),0).toString()+"'";
									int result = statementUpdate(connection, updateSql);
									if(result!=0){
										JOptionPane.showMessageDialog(null, "修改成绩成功");
									}else{
										JOptionPane.showMessageDialog(null, "修改成绩失败");
									}
								}
							}
						});
					checkTab.setModel(dtm);
					JScrollPane scrollpane = new JScrollPane(checkTab);
					scrollpane.setBounds(20,20, 500, 160);
					tablePanel.setLayout(null);
					tablePanel.add(scrollpane);
					tablePanel.setVisible(true);
				}else{
					JOptionPane.showMessageDialog(null, "请输入课程名");
				}
			}
		});
		checkTab.addMouseListener(new MouseAdapter(){//点击过的单元格
			public void mouseClicked(MouseEvent e){
	           //记录进入编辑状态前单元格得数据
	        	 //第i行的数据放入第i个数中
	           if(checkTab.getSelectedColumn()==2 && oldScore[checkTab.getSelectedRow()]==null) {
	        	   oldScore[checkTab.getSelectedRow()] = checkTab.getValueAt(checkTab.getSelectedRow(),checkTab.getSelectedColumn()).toString();
	           }
	         }     
         });
	}
	private User modifyAdMinPassWord(final Connection connection, AdminFrame adminFrame) {//修改管理员密码
		jpanel = new JPanel();
		JPanel leftJpanel,rightJpanel,titleJpanel,btnJpanel;
		final JButton editBtn,modifyBtn;
		JLabel addLabel;
		final JTextField username;
		final JPasswordField oldPassword,newPassword,confirmPassword;
		leftJpanel = new JPanel(new GridLayout(6,1));
		rightJpanel = new JPanel(new GridLayout(6,1));
		titleJpanel = new JPanel();
		btnJpanel = new JPanel();
		editBtn = new JButton("编辑");
		modifyBtn = new JButton("修改");
		addLabel = new JLabel("个人中心",SwingConstants.CENTER);
		
		username = new JTextField(20);
		oldPassword = new JPasswordField();
		newPassword = new JPasswordField();
		confirmPassword = new JPasswordField();
		
		leftJpanel.add(new JLabel("用户名",SwingConstants.CENTER));
		leftJpanel.add(new JLabel("请输入原密码",SwingConstants.CENTER));
		leftJpanel.add(new JLabel("请输入新密码",SwingConstants.CENTER));
		leftJpanel.add(new JLabel("再次输入新密码",SwingConstants.CENTER));
		
		rightJpanel.add(username);
		rightJpanel.add(oldPassword);
		rightJpanel.add(newPassword);
		rightJpanel.add(confirmPassword);
		
		username.setEditable(false);
		oldPassword.setEnabled(false);
		newPassword.setEnabled(false);
		confirmPassword.setEnabled(false);
		modifyBtn.setEnabled(false);
		
		titleJpanel.add(addLabel);
		btnJpanel.add(editBtn);
		btnJpanel.add(modifyBtn);
		
		titleJpanel.setBounds(170,0,200,45);
		leftJpanel.setBounds(20,45,100,200);
		rightJpanel.setBounds(150,45,300,200);
		btnJpanel.setBounds(170,270,200,50);
		jpanel.add(titleJpanel);
		jpanel.add(leftJpanel);
		jpanel.add(rightJpanel);
		jpanel.add(btnJpanel);
		jpanel.setLayout(null);
		adminFrame.add(jpanel);
		adminFrame.setVisible(true);
		adminFrame.setSize(550,400);
		
		final String currentUsername = LoginFrame.currentUser.getUsername();
		username.setText(currentUsername);
		
		modifyBtn.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				String oldSQL = "select password from administrator where username = '"+currentUsername+"'";
				String newName = username.getText(),
						oldPss = new String(oldPassword.getPassword()),
						newPss = new String(newPassword.getPassword()),
						confirmPss = new String(confirmPassword.getPassword());
				
				if(StringUtil.isEmpty(newName)){
					JOptionPane.showMessageDialog(null, "用户名不能为空");
					return;
				}
				if(StringUtil.isEmpty(oldPss)){
					JOptionPane.showMessageDialog(null, "请输入原密码");
					return;
				}
				if(StringUtil.isEmpty(newPss)){
					JOptionPane.showMessageDialog(null, "新密码不能为空");
					return;
				}
				if(StringUtil.isEmpty(confirmPss)){
					JOptionPane.showMessageDialog(null, "确认新密码");
					return;
				}
				try{
					ResultSet result = statementQuery(connection, oldSQL);
					if(result.next()){
						if(oldPss.equals(result.getString("password")) && newPss.equals(confirmPss)){
							String passUpdateSql = "update administrator set username = '"+newName+"' ,password = '"+newPss+"' where username='"+currentUsername+"'";
							int userUpdate = statementUpdate(connection, passUpdateSql);
							if(userUpdate!=0){
								JOptionPane.showMessageDialog(null, "信息修改成功");
							}
						}
					}
				}catch(Exception e1){
					e1.getStackTrace();
				}
			}
		});
		
		editBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				username.setEditable(true);
				oldPassword.setEnabled(true);
				newPassword.setEnabled(true);
				confirmPassword.setEnabled(true);
				editBtn.setEnabled(true);
				modifyBtn.setEnabled(true);
			}
		});
		return LoginFrame.currentUser;
	}
	private void chooseCourse(final Connection connection, UserFrame userFrame) {//选课
		jpanel = new JPanel();
		JPanel leftJpanel,rightJpanel,titleJpanel,btnJpanel;
		JButton searchSno;
		final JButton selectBtn;
		JLabel addLabel;
		final JTextField searchCname,sno;
		final JComboBox<String> cnameBox;
		final String[] cnameText=new String[1];
		
		leftJpanel = new JPanel(new GridLayout(6,1));
		rightJpanel = new JPanel(new GridLayout(6,1));
		titleJpanel = new JPanel();
		btnJpanel = new JPanel();
		selectBtn = new JButton("选课");
		searchSno = new JButton("搜索");
		addLabel = new JLabel("选课",SwingConstants.CENTER);
		searchCname = new JTextField(20);
		sno = new JTextField(20);
		cnameBox =  new JComboBox<String>();
		
		sno.setText(LoginFrame.currentUser.getSno());

		leftJpanel.add(new JLabel("请输入选课名",SwingConstants.CENTER));
		leftJpanel.add(new JLabel("学号",SwingConstants.CENTER));
		leftJpanel.add(new JLabel("课程名",SwingConstants.CENTER));
		
		rightJpanel.add(searchCname);
		rightJpanel.add(sno);
		rightJpanel.add(cnameBox);
		
		sno.setEditable(false);
		cnameBox.setEnabled(false);
		selectBtn.setEnabled(false);
		
		titleJpanel.add(addLabel);
		btnJpanel.add(selectBtn);
		
		searchSno.setBounds(460, 53, 60, 23);
		titleJpanel.setBounds(170,0,200,45);
		leftJpanel.setBounds(20,45,100,200);
		rightJpanel.setBounds(150,45,300,200);
		btnJpanel.setBounds(170,270,200,50);
		jpanel.add(searchSno);
		jpanel.add(titleJpanel);
		jpanel.add(leftJpanel);
		jpanel.add(rightJpanel);
		jpanel.add(btnJpanel);
		jpanel.setLayout(null);
		userFrame.add(jpanel);
		userFrame.setVisible(true);
		userFrame.setSize(550,400);
		
		searchSno.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cnameText[0] = searchCname.getText();
				if(cnameText[0]!= null){
					String searchSql = "select cname from course where cname ='"+cnameText[0]+"'";
					try {
						ResultSet result = statementQuery(connection, searchSql),
								resultBackup = statementQuery(connection, searchSql);
						if(resultBackup.next()){
							cnameBox.removeAllItems();
							while(result.next()){
								cnameBox.addItem(result.getString("cname"));
							}
							selectBtn.setEnabled(true);
							cnameBox.setEnabled(true);
							cnameBox.setSelectedIndex(0);
						}else{
							JOptionPane.showMessageDialog(null, "不存在该课程");
						}
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				}else{
					JOptionPane.showMessageDialog(null, "请输入选修课程名");
				}
			}
		});
		selectBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(StringUtil.isEmpty(sno.getText())){
					JOptionPane.showMessageDialog(null, "学号不能为空");
					return ;
				}
				if(StringUtil.isEmpty((String)cnameBox.getSelectedItem())){
					JOptionPane.showMessageDialog(null, "课程名不能为空");
					return ;
				}
				String checkChooseCourseSql = "select course.cno from course,student_course where sno = '"+sno.getText()+"' and course.cno = student_course.cno and cname = '"+ cnameBox.getSelectedItem().toString() +"'";//查看改学生是否已选该课程
				ResultSet checkResult;
				try {
					checkResult = statementQuery(connection, checkChooseCourseSql);
					if(checkResult.next()){//若已选改门课程
						JOptionPane.showMessageDialog(null, "您已选该课程，无法重复选课！");
						return ;
					}else{
						String cno="";
						try {
							String cnoSQL = "select cno from course where cname = '"+ cnameBox.getSelectedItem().toString()+"'";
							ResultSet cnoResult = statementQuery(connection, cnoSQL);
							if(cnoResult.next()){
								cno = cnoResult.getString("cno");
								if(cno == null){
									cno = "";
								}
							}
							
							String chooseCourseSql = "insert into student_course(cno,sno) values ('"+ cno +"','"+sno.getText() +"')";
							int chooseResult = statementUpdate(connection, chooseCourseSql);
							if(chooseResult!=0){
								JOptionPane.showMessageDialog(null, "选课成功");
							}else{
								JOptionPane.showMessageDialog(null, "选课失败");
							}
						} catch (SQLException e2) {
							e2.printStackTrace();
						}
					}
				} catch (SQLException e1) {
					JOptionPane.showMessageDialog(null, "选课失败");
					e1.printStackTrace();
				}
			}
		});
	}
	private void checkPersonalInfo(final Connection connection, UserFrame userFrame) {//修改个人信息
		jpanel = new JPanel();
		JPanel leftJpanel,rightJpanel,titleJpanel,btnJpanel;
		final JButton editBtn;
		final JButton modifyBtn;
		JLabel addLabel;
		final JTextField sno,sname,sbirthday;
		final JComboBox<String>  ssex;
		final JComboBox<String> dnoBox;
		final JComboBox<String> spnoBox; 
		
		leftJpanel = new JPanel(new GridLayout(6,1));
		rightJpanel = new JPanel(new GridLayout(6,1));
		titleJpanel = new JPanel();
		btnJpanel = new JPanel();
		editBtn = new JButton("编辑");
		modifyBtn = new JButton("修改");
		
		addLabel = new JLabel("查看信息",SwingConstants.CENTER);
		
		sno = new JTextField(20);
		sname = new JTextField(20);
		ssex = new JComboBox<String>();
		dnoBox =  new JComboBox<String>();
		spnoBox =new JComboBox<String>();
		sbirthday = new JTextField(20);
		
		ssex.addItem("男");
		ssex.addItem("女");
		
		try {
			String dnoSQL = "select dno from department order by dno";
			
			ResultSet dnoItem = statementQuery(connection, dnoSQL);
			while(dnoItem.next()){
				dnoBox.addItem(dnoItem.getString(1));
			}
		} catch (SQLException e2) {
			e2.printStackTrace();
		} 
		createSpnoItem(connection, spnoBox, (String)dnoBox.getSelectedItem());
		
		dnoBox.setSelectedIndex(0);
		leftJpanel.add(new JLabel("请输入修改的学号",SwingConstants.CENTER));
		leftJpanel.add(new JLabel("姓名",SwingConstants.CENTER));
		leftJpanel.add(new JLabel("性别",SwingConstants.CENTER));
		leftJpanel.add(new JLabel("系号",SwingConstants.CENTER));
		leftJpanel.add(new JLabel("专业号",SwingConstants.CENTER));
		leftJpanel.add(new JLabel("出生日期",SwingConstants.CENTER));
		
		rightJpanel.add(sno);
		rightJpanel.add(sname);
		rightJpanel.add(ssex);
		rightJpanel.add(dnoBox);
		rightJpanel.add(spnoBox);
		rightJpanel.add(sbirthday);
		
		sno.setEditable(false);
		sname.setEditable(false);
		ssex.setEnabled(false);
		dnoBox.setEnabled(false);
		spnoBox.setEnabled(false);
		sbirthday.setEditable(false);
		editBtn.setEnabled(false);
		modifyBtn.setEnabled(false);
		
		titleJpanel.add(addLabel);
		btnJpanel.add(editBtn);
		btnJpanel.add(modifyBtn);
		
		titleJpanel.setBounds(170,0,200,45);
		leftJpanel.setBounds(20,45,100,200);
		rightJpanel.setBounds(150,45,300,200);
		btnJpanel.setBounds(170,270,200,50);
		jpanel.add(titleJpanel);
		jpanel.add(leftJpanel);
		jpanel.add(rightJpanel);
		jpanel.add(btnJpanel);
		jpanel.setLayout(null);
		userFrame.add(jpanel);
		userFrame.setVisible(true);
		userFrame.setSize(550,400);
		
				//文本框加入提示语
		sbirthday.addFocusListener(new FocusListener(){
	    	@Override
			public void focusLost(FocusEvent e) {
	    		if(StringUtil.isEmpty(sbirthday.getText())){
	    			sbirthday.setText("格式如：2000/01/01");
	    		}
	    	}
			@Override
			public void focusGained(FocusEvent e) {}
	    });
		String searchSql = "select * from studentinfo where sno = '" +LoginFrame.currentUser.getSno()+"'";
		try {
			ResultSet result = statementQuery(connection, searchSql);
			if(result.next()){
				sno.setText(result.getString("sno"));
				sname.setText(result.getString("sname"));
				ssex.setSelectedItem(result.getString("ssex"));
				dnoBox.setSelectedItem(result.getString("dno"));
				spnoBox.setSelectedItem(result.getString("spno"));
				sbirthday.setText(result.getString("sbirthday"));
				editBtn.setEnabled(true);
			}else{
				JOptionPane.showMessageDialog(null, "不存在该学号");
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		editBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sname.setEditable(true);
				ssex.setEnabled(true);
				sbirthday.setEditable(true);
				modifyBtn.setEnabled(true);
			}
		});
		modifyBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String updateSql;
				if(StringUtil.isEmpty(sno.getText())){
					JOptionPane.showMessageDialog(null, "学号不能为空");
					return ;
				}
				if(StringUtil.isEmpty(sname.getText())){
					JOptionPane.showMessageDialog(null, "姓名不能为空");
					return ;
				}
				updateSql = "update studentinfo set sname = '"+sname.getText() +"' ,ssex = '"+ssex.getSelectedItem().toString()+"',sbirthday='"+sbirthday.getText()+"' where sno='"+sno.getText()+"'";
				int result = statementUpdate(connection, updateSql);
				if(result!=0){
					JOptionPane.showMessageDialog(null, "修改成功");
					sname.setEditable(false);
					ssex.setEnabled(false);
					sbirthday.setEditable(false);
					modifyBtn.setEnabled(false);
				}
			}
		});
	}
	private User modifyUserPassWord(final Connection connection, UserFrame userframe) {//修改用户密码
		jpanel = new JPanel();
		JPanel leftJpanel,rightJpanel,titleJpanel,btnJpanel;
		final JButton editBtn,modifyBtn;
		JLabel addLabel;
		final JTextField username,sno;
		final JPasswordField oldPassword,newPassword,confirmPassword;
		leftJpanel = new JPanel(new GridLayout(6,1));
		rightJpanel = new JPanel(new GridLayout(6,1));
		titleJpanel = new JPanel();
		btnJpanel = new JPanel();
		editBtn = new JButton("编辑");
		modifyBtn = new JButton("修改");
		
		addLabel = new JLabel("个人中心",SwingConstants.CENTER);
		
		sno = new JTextField(20);
		username = new JTextField(20);
		oldPassword = new JPasswordField();
		newPassword = new JPasswordField();
		confirmPassword = new JPasswordField();
		
		leftJpanel.add(new JLabel("学号",SwingConstants.CENTER));
		leftJpanel.add(new JLabel("用户名",SwingConstants.CENTER));
		leftJpanel.add(new JLabel("请输入原密码",SwingConstants.CENTER));
		leftJpanel.add(new JLabel("请输入新密码",SwingConstants.CENTER));
		leftJpanel.add(new JLabel("再次输入新密码",SwingConstants.CENTER));
		
		rightJpanel.add(sno);
		rightJpanel.add(username);
		rightJpanel.add(oldPassword);
		rightJpanel.add(newPassword);
		rightJpanel.add(confirmPassword);
		
		sno.setEditable(false);
		username.setEditable(false);
		oldPassword.setEnabled(false);
		newPassword.setEnabled(false);
		confirmPassword.setEnabled(false);
		modifyBtn.setEnabled(false);
		
		titleJpanel.add(addLabel);
		btnJpanel.add(editBtn);
		btnJpanel.add(modifyBtn);
		
		titleJpanel.setBounds(170,0,200,45);
		leftJpanel.setBounds(20,45,100,200);
		rightJpanel.setBounds(150,45,300,200);
		btnJpanel.setBounds(170,270,200,50);
		jpanel.add(titleJpanel);
		jpanel.add(leftJpanel);
		jpanel.add(rightJpanel);
		jpanel.add(btnJpanel);
		jpanel.setLayout(null);
		userframe.add(jpanel);
		userframe.setVisible(true);
		userframe.setSize(550,400);
		
		final String currentUsername = LoginFrame.currentUser.getUsername();
		username.setText(currentUsername);
		sno.setText(LoginFrame.currentUser.getSno());
		
		modifyBtn.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				String oldSQL = "select password from user where username = '"+currentUsername+"'";
				String newName = username.getText(),
						oldPss = new String(oldPassword.getPassword()),
						newPss = new String(newPassword.getPassword()),
						confirmPss = new String(confirmPassword.getPassword());
				
				if(StringUtil.isEmpty(newName)){
					JOptionPane.showMessageDialog(null, "用户名不能为空");
					return;
				}
				if(StringUtil.isEmpty(oldPss)){
					JOptionPane.showMessageDialog(null, "请输入原密码");
					return;
				}
				if(StringUtil.isEmpty(newPss)){
					JOptionPane.showMessageDialog(null, "新密码不能为空");
					return;
				}
				if(StringUtil.isEmpty(confirmPss)){
					JOptionPane.showMessageDialog(null, "确认新密码");
					return;
				}
				try{
					ResultSet result =statementQuery(connection, oldSQL);
					if(result.next()){
						if(oldPss.equals(result.getString("password")) && newPss.equals(confirmPss)){
							String passUpdateSql = "update user set username = '"+newName+"' ,password = '"+newPss+"' where username='"+currentUsername+"'";
							int userUpdate = statementUpdate(connection, passUpdateSql);
							if(userUpdate!=0){
								JOptionPane.showMessageDialog(null, "信息修改成功");
								oldPassword.setText("");
								newPassword.setText("");
								confirmPassword.setText("");
								username.setEditable(false);
								oldPassword.setEnabled(false);
								newPassword.setEnabled(false);
								confirmPassword.setEnabled(false);
								modifyBtn.setEnabled(false);
							}
						}
					}
				}catch(Exception e1){
					e1.getStackTrace();
				}
			}
		});
		
		editBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				username.setEditable(true);
				oldPassword.setEnabled(true);
				newPassword.setEnabled(true);
				confirmPassword.setEnabled(true);
				modifyBtn.setEnabled(true);
			}
		});
		return LoginFrame.currentUser;
	}
	private void checkPersonScore(Connection connection,UserFrame adminFrame) throws SQLException{//查看学生信息
		jpanel = new JPanel();
		Object[][] object = null;
		int rowCount = 0;
		ResultSet result = null;
		String[] columnNames =  { "课程名","成绩"};
		
		try{
			String sql = "select course.cname,score from course,student_course where sno = '"+ LoginFrame.currentUser.getSno()+"' and course.cno = student_course.cno";
			result = statementQuery(connection,sql);
			//获取行数 
			String countsql = "select count(*) totalCount from student_course where sno = '"+ LoginFrame.currentUser.getSno()+"'"; 
			ResultSet rset = statementQuery(connection, countsql);
			if(rset.next()) { 
				rowCount=rset .getInt("totalCount"); 
			} 
			object = new Object[rowCount][2];
			int i=0, j=0;
			while(result.next()){
				if(result.getString(1) != null){
					for(j = 0;j<2;j++){
						object[i][j]=result.getString(j+1);
					}
					i++;
				}
			}
		}catch (SQLException e) { 
			   e.printStackTrace();
		  }
		JTable checkTab = new JTable(object,columnNames);
		checkTab.setEnabled(false);
		JScrollPane scrollpane = new JScrollPane(checkTab);
		
		scrollpane.setBounds(20,50,500,200);
		jpanel.setLayout(null);
		jpanel.add(scrollpane);
		
		JLabel tableTitle = new JLabel();
		tableTitle.setText("个人成绩");
		tableTitle.setForeground(Color.BLUE);
		tableTitle.setHorizontalAlignment(SwingConstants.CENTER);
		tableTitle.setBounds(200, 20, 150, 20);
		jpanel.add(tableTitle);
		jpanel.setVisible(true);
		
		adminFrame.add(jpanel);
		adminFrame.setSize(550,350);
		adminFrame.setResizable(false);
		
	}
	private void createSpnoItem(Connection connection,JComboBox<String> spnoBox,String dno){//动态创建MenuItem
		PreparedStatement pstmt;
		spnoBox.removeAllItems();;
		try {
			pstmt = (PreparedStatement) connection.prepareStatement("select distinct spno from speciality where dno = ? ");
			pstmt.setString(1,dno);
			ResultSet spno = pstmt.executeQuery();
			while(spno.next()){
				spnoBox.addItem(spno.getString(1));
			}
			spnoBox.setSelectedIndex(0);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}
	/**
	 * 根据selection作相应的操作
	 * @param selection
	 */
	public void performAction(DbUtil dbUtil, JFrame frame, String selection) {
		if (jpanel != null) {
			frame.remove(jpanel);
		}
		try{
			switch(selection) {
			case AdminFrame.CHECK_STUDENT_INFO:
				checkStudentInfo(dbUtil.getCon(), (AdminFrame) frame);
				break;
			case AdminFrame.ADD_STUDENT_INFO:
				addStudentInfo(dbUtil.getCon(),(AdminFrame) frame);
				break;
			case AdminFrame.MODIFY_DELETE_STUDENT_INFO:
				modifyStudentInfo(dbUtil.getCon(),(AdminFrame) frame);
				break;
			case AdminFrame.ADD_COURSE:
				addCourse(dbUtil.getCon(),(AdminFrame) frame);
				break;
			case AdminFrame.MODIFY_DELETE_COURSE:
				modifyCourse(dbUtil.getCon(),(AdminFrame) frame);
				break;
			case AdminFrame.EDIT_STUDENT_SCORE:
				editScore(dbUtil.getCon(),(AdminFrame) frame);
				break;
			case AdminFrame.MODIFY_PASSWORD:
				modifyAdMinPassWord(dbUtil.getCon(),(AdminFrame) frame);
				break;
			case UserFrame.CHECK_MODIFY_PERSONAL_INFO:
				checkPersonalInfo(dbUtil.getCon(),(UserFrame) frame);
				break;
			case UserFrame.CHOOSE_COURSE:
				chooseCourse(dbUtil.getCon(),(UserFrame) frame);
				break;
			case UserFrame.PRINT_SCORE:
				checkPersonScore(dbUtil.getCon(),(UserFrame) frame);
				break;
			case UserFrame.MODIFY_PERSONAL:
				modifyUserPassWord(dbUtil.getCon(),(UserFrame)frame);
				break;
			}
		}catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, "操作失败");
			}
	}
	private ResultSet statementQuery(Connection connection,String sql){
		ResultSet result=null;
		try {
			PreparedStatement statement = (PreparedStatement)connection.prepareStatement(sql);
			result = statement.executeQuery();
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "操作失败");
			e.printStackTrace();
		}
		return result;
	}
	private int statementUpdate(Connection connection,String sql){
		int result=0;
		try {
			PreparedStatement statement = (PreparedStatement)connection.prepareStatement(sql);
			result = statement.executeUpdate();
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "操作失败");
			e.printStackTrace();
		}
		return result;
	}
}
