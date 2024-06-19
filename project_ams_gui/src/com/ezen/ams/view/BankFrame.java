package com.ezen.ams.view;

import com.ezen.ams.model.Account;
import com.ezen.ams.model.AccountService;
import com.ezen.ams.model.AccountType;
import com.ezen.ams.model.MinusAccount;
import com.ezen.ams.util.Validator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Collection;
import java.util.List;

public class BankFrame extends Frame {
    private AccountService accountService;

    /**
     * 계좌 관리 프로그램을 GUI로 구현
     */
    Choice accountChoice , accountListSort;
    TextField accountNumTF, accountOwnerTF, passwdTF, depositMoneyTF, borrowMoneyTF;
    Button checkButton, deleteButton, searchButton, newAccountButton, checkAllButton;
    TextArea listTA;
    Label accountKindLabel, accountNumLabel, accountOwnerLabel, passwdLabel, depositMoneyLabel, borrowMoneyLabel, accountListLabel, unitLabel;

    public BankFrame(String title){
        super(title);
        accountService = new AccountService();
        accountService.addAccount(new Account("1111-2222","신연재",1111,10000));
        accountService.addAccount(new Account("1111-3333","이연재",1111,20000));
        accountService.addAccount(new MinusAccount("2222-3333","홍길동",2222,10000,50000));

        accountKindLabel = new Label("계좌종류");
        accountChoice = new Choice();
        accountChoice.add("-계좌종류 선택-");
        AccountType[] accountTypes = AccountType.values();
        for (AccountType accountType : accountTypes) {
            accountChoice.add(accountType.getName());
        }
        accountNumLabel = new Label("계좌번호");
        accountNumTF = new TextField();
        accountOwnerLabel = new Label("예금주명");
        accountOwnerTF = new TextField();
        passwdLabel = new Label("비밀번호");
        passwdTF = new TextField();
        depositMoneyLabel = new Label("입금금액");
        depositMoneyTF = new TextField();
        borrowMoneyLabel = new Label("대출금액");
        borrowMoneyTF = new TextField();
        checkButton = new Button("조 회");
        deleteButton = new Button(" 삭 제");
        searchButton = new Button("검 색");
        newAccountButton = new Button("신규등록");
        checkAllButton = new Button("전체조회");
        accountListLabel = new Label("계좌목록");
        listTA = new TextArea();
        accountListSort = new Choice();
        accountListSort.add("계좌 정렬");
        unitLabel = new Label("(단위 : 원)");
        listTA.setBackground(new Color(240, 240, 240));
    }


    public AccountService getAccountService() {
        return accountService;
    }

    public void initComponents(){

        setLayout(null);
        accountKindLabel.setBounds(35, 45, 50, 30);
        accountChoice.setBounds(100, 50, 120, 30);
        accountNumLabel.setBounds(35, 75, 50, 30);
        accountNumTF.setBounds(100, 80, 200, 25);
        checkButton.setBounds(310, 80, 70, 25);
        deleteButton.setBounds(385, 80, 70, 25);
        accountOwnerLabel.setBounds(35, 110, 50, 30);
        accountOwnerTF.setBounds(100, 115, 200, 25);
        searchButton.setBounds(310, 115, 70, 25);
        passwdLabel.setBounds(35, 145, 50, 30);
        passwdTF.setBounds(100, 150, 200, 25);
        depositMoneyLabel.setBounds(315, 145, 50, 30);
        depositMoneyTF.setBounds(385, 150, 200, 25);
        borrowMoneyLabel.setBounds(35, 180, 50, 30);
        borrowMoneyTF.setBounds(100, 185, 200, 25);
        newAccountButton.setBounds(310, 185, 70, 25);
        checkAllButton.setBounds(385, 185, 70, 25);
        accountListLabel.setBounds(35, 220, 50, 30);
        accountListSort.setBounds(435,220,90,30);
        unitLabel.setBounds(525 , 220, 60, 30);
        listTA.setBounds(30, 250, 555, 170);

        add(accountListSort);
        add(accountKindLabel);
        add(accountChoice);
        add(accountNumLabel);
        add(accountNumTF);
        add(checkButton);
        add(deleteButton);
        add(accountOwnerLabel);
        add(accountOwnerTF);
        add(searchButton);
        add(passwdLabel);
        add(passwdTF);
        add(depositMoneyLabel);
        add(depositMoneyTF);
        add(borrowMoneyLabel);
        add(borrowMoneyTF);
        add(newAccountButton);
        add(checkAllButton);
        add(accountListLabel);
        add(unitLabel);
        add(listTA);

        setEnable(false);
        printAccounts();
    }

    /**
     * 각각의 이벤트소스에 이벤트 리스너 등록
     */
    public void eventRegister(){

        /**
         * 프로그램 종료 처리
         */
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exit();
            }
        });

        /**
         * 신규계좌 등록 처리
         */
        newAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openAccount();
            }
        });

        /**
         * 계좌 종류 선택 처리
         */
        accountChoice.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() ==ItemEvent.SELECTED){
                    selectAccountType();
                }
            }
        });

        /**
         * 전체 계좌 출력 처리
         */
        checkAllButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                printAccounts();
            }
        });

        /**
         * 모든 텍스트 필드 포커스 처리
         */
        Component[] components = getComponents();
        for (Component component : components) {
            if (component instanceof TextField){
                component.addFocusListener(new FocusAdapter() {
                    @Override
                    public void focusGained(FocusEvent e) {
                        ((TextField) component).setText("");
                    }
                });
            }
        }

        /**
         * 계좌번호 조회 처리
         */
        checkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                findByAccountNumber();
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                findByAccountOwner();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteAccount();
            }
        });
    }

    /**
     * 계좌 등록 기능 처리
     */
    private void openAccount(){
        String accountNumber = null;
        String accountOwner = null;
        int password = 0;
        long restMoney = 0L;
        long borrowMoney =0L;

        accountNumber = accountNumTF.getText();
        accountOwner = accountOwnerTF.getText();
        String inputPasswd = passwdTF.getText();
        String inputMoney = depositMoneyTF.getText();

       if (!Validator.isText(accountNumber)){
           fieldError(accountNumTF,"계좌번호는 필수입력 사항입니다.");
       }
        if (!Validator.isText(accountOwner)){
            fieldError(accountOwnerTF,"예금주명은 필수입력 사항입니다.");
        }
        if(!Validator.isText(inputPasswd)){
            fieldError(passwdTF, "필수입력 항목입니다.");
        }

        if(!Validator.isText(inputMoney)){
            fieldError(depositMoneyTF, "필수입력 항목입니다.");
        }

        /**
         * 입력된 정보의 2차 검증 : 유효한 입력 형식인지 검증
         */
        if(!Validator.isNumber(inputPasswd)){
            fieldError(passwdTF, "숫자형식이어야 합니다.");
        }

        if(!Validator.isNumber(inputMoney)){
            fieldError(depositMoneyTF, "숫자형식이어야 합니다.");
        }
        password = Integer.parseInt(inputPasswd);
        restMoney = Long.parseLong(inputMoney);

        JOptionPane.showMessageDialog(null,"※생성이 완료 되었습니다.","안내창",JOptionPane.INFORMATION_MESSAGE);


        /**
         * 신규 계좌 생성 처리
         * 입출금 계좌와 마이너스 계좌를 구분하여 생성
         */
        Account newAccount = null;
        /**
         * 입출금 계좌인 경우와 마이너스 계좌인 경우를 분기
          */
        if(accountChoice.getSelectedItem().equals(AccountType.valueOf("ACCOUNT").getName())){
            newAccount = new Account(accountNumber, accountOwner, password, restMoney);
        }else if(accountChoice.getSelectedItem().equals(AccountType.valueOf("MINUS_ACCOUNT").getName())){
            String inputBorrowMoney = borrowMoneyTF.getText();
            /**
             * 입력된 정보의 2차 검증 : 유효한 입력 형식인지 검증
             */
            if(!Validator.isText(inputBorrowMoney)){
                fieldError(borrowMoneyTF, "필수입력 항목입니다.");
            }

            if(!Validator.isNumber(inputBorrowMoney)){
                fieldError(borrowMoneyTF, "숫자형식이어야 합니다.");
            }
            borrowMoney = Long.parseLong(inputBorrowMoney);
            newAccount = new MinusAccount(accountNumber, accountOwner, password, restMoney, borrowMoney);
        }
        accountService.addAccount(newAccount);

        /**
         * 사용자에게 신규 계좌 등록 메시지 전달
         */
           showMessage("※ 신규계좌("+newAccount.getAccountNum()+")가 등록 되었습니다.");

        /**
         * 신규 계좌 생성 후 작성된 텍스트를 삭제(편의성)
         */
          setClearField();
       }

    /**
     * 테이블 헤더 출력 기능 처리
     */
    private void printHeader(){
        listTA.append("=====================================================================================\n");
        String header = String.format("%1$-20s%2$-20s%3$-15s%4$-15s%5$-15s","계좌종류","계좌번호","예금주","잔액","대출금액");
        listTA.append(header+"\n");
        listTA.append("=====================================================================================\n");
    }
    /**
     * 사용자에게 한번에 모든 정보를 출력해서 보여주기 위한 메소드
     * @param accountType 출력하고자 하는 타입
     * @param accountNum 출력하고자 하는 계좌번호
     * @param accountOwner 출력하고자 하는 예금주 명
     * @param balance 출력하고자 하는 잔액
     * @param borrowMoney 출력하고자 하는 대출금액 (입출금 계좌인 경우만)
     */
    private void printRow(String accountType,String accountNum,String accountOwner,long balance,long borrowMoney){
        String row = String.format("%1$-20s%2$-20s%3$-15s%4$,-15d%5$,-15d",accountType,accountNum,accountOwner,balance,borrowMoney);
        listTA.append(row+"\n");
    }

    /**
     * 전체 계좌 조회 기능 처리
     */
    private void printAccounts(){
        listTA.setText("  ");
        Collection<Account> accounts = accountService.getAccounts();
        if(accounts.isEmpty()){
            JOptionPane.showMessageDialog(null,"※등록된 계좌가 존재하지 않습니다.","검색결과",JOptionPane.ERROR_MESSAGE);
            return;
        }
        printHeader();
        for (Account account : accounts) {
            String accountType = null;
            String accountNum = null;
            String accountOwner = null;
            long balance = 0L;
            long borrowMoney =0L;
            if (account instanceof MinusAccount ){
                accountType = AccountType.valueOf("MINUS_ACCOUNT").getName();
                borrowMoney = ((MinusAccount) account).getBorrowMoney();
            }else {
                accountType = AccountType.valueOf("ACCOUNT").getName();
            }
            accountNum = account.getAccountNum();
            accountOwner = account.getAccountOwner();
            balance =account.getBalance();

            String row = String.format("%1$-20s%2$-20s%3$-15s%4$,-15d%5$,-15d",accountType,accountNum,accountOwner,balance,borrowMoney);
            listTA.append(row+"\n");
        }
    }

    /**
     * 계좌번호로 계좌를 검색하는 기능 처리
     */
    private void findByAccountNumber(){
        if(!accountNumTF.isEnabled()) {
            accountNumTF.setEnabled(true);
            accountNumTF.requestFocus();
        }
      String findAccountNumber =accountNumTF.getText();
        if (!Validator.isText(findAccountNumber)){
            fieldError(accountNumTF,"계좌번호를 입력하세요.");
            return;
        }else {
           Account findAccount = accountService.findAccount(findAccountNumber);
           if (findAccount != null){
               listTA.setText("");
               printHeader();
               String accountType = null;
               long borrowMoney = 0L;
               if(findAccount instanceof MinusAccount){
                   accountType = AccountType.valueOf("MINUS_ACCOUNT").getName();
                   borrowMoney =((MinusAccount)findAccount).getBorrowMoney();
               }else {
                   accountType = AccountType.valueOf("ACCOUNT").getName();
                   borrowMoney = 0L;
               }
               printRow(accountType,findAccount.getAccountNum(),findAccount.getAccountOwner(),findAccount.getBalance(),borrowMoney);
           }else {
               showMessage("해당 계좌가 존재하지 않습니다.");
           }
        }
    }

    /**
     * 예금주 이름으로 계좌를 검색하는 기능 처리
     */
    private void findByAccountOwner() {
        if (!accountOwnerTF.isEnabled()) {
            accountOwnerTF.setEnabled(true);
            accountOwnerTF.requestFocus();
        }
        String findAccountOwner = accountOwnerTF.getText();
        if (!Validator.isText(findAccountOwner)){
            fieldError(accountOwnerTF,"예금주를 입력하세요.");
        }else {
            List<Account> findAccounts = accountService.findAccountByAccountOwner(findAccountOwner);
            if (!findAccounts.isEmpty()) {
                listTA.setText("");
                printHeader();
                for (Account findAccount : findAccounts) {
                    String accountType = null;
                    String accountNum = null;
                    String accountOwner = null;
                    long balance = 0L;
                    long borrowMoney = 0L;
                    if (findAccount instanceof MinusAccount) {
                        accountType = AccountType.valueOf("MINUS_ACCOUNT").getName();
                        borrowMoney = ((MinusAccount) findAccount).getBorrowMoney();
                    } else {
                        accountType = AccountType.valueOf("ACCOUNT").getName();
                        borrowMoney = 0L;
                    }
                    printRow(accountType, findAccount.getAccountNum(), findAccount.getAccountOwner(), findAccount.getBalance(), borrowMoney);
                }
            }  else showMessage("※해당 계좌가 존재하지 않습니다.");
        }
    }
        /**
         *  유효성 검증 에러메시지 출력
         * @param tf 에러 메시지를 출력하고자 하는 컴포넌트
         * @param errorMessage 출력하고자 하는 에러메시지
         */

       private void fieldError(TextField tf ,String errorMessage){
           tf.setText(errorMessage);
       }

    /**
     * 사용자에게 메시지를 출력해서 보여주는 기능 처리
     * @param message 사용자에게 보여주고자 하는 메시지
     */
    private void showMessage(String message){
        listTA.setForeground(Color.BLACK);
        listTA.setText(message);
    }

    /**
     * 사용자에게 오류 메시지를 보여주는 기능 처리
     * @param errorMessage 사용자에게 보여지는 오류메시지
     */
    private void showErrorMessage(String errorMessage){
        listTA.setText(errorMessage);
    }

    /**
     * 사용자 화면에서 계좌 종류 선택 기능 처리
     */
    private void selectAccountType() {
        if (accountChoice.getSelectedIndex() == 0) {
            setEnable(false);
        } else {
            String accountType = accountChoice.getSelectedItem();
            accountNumTF.setEnabled(true);
            accountOwnerTF.setEnabled(true);
            passwdTF.setEnabled(true);
            depositMoneyTF.setEnabled(true);
            if (accountType.equals(AccountType.valueOf("ACCOUNT").getName())) {
                borrowMoneyTF.setEnabled(false);
            } else if (accountType.equals(AccountType.valueOf("MINUS_ACCOUNT").getName())) {
                borrowMoneyTF.setEnabled(true);
            }
        }
     }

     private void AccountSort(){
         if (accountListSort.getSelectedIndex()!=0) {
             String accountSet = accountListSort.getSelectedItem();


         }
         return;


     }








    /**
     * 전체 컴포넌트 비활성화 기능 처리
     * @param enable 컴포넌트 비활성화 처리
     */
        private void setEnable ( boolean enable){
            Component[] components = getComponents();
            for (Component component : components) {
                if (component instanceof TextField) {
                    component.setEnabled(enable);
                }
            }
        }

    /**
     * 선택된 계좌를 삭제하는 기능 처리
     */
        private void deleteAccount() {
            if (!accountNumTF.isEnabled()) {
                accountNumTF.setEnabled(true);
                accountNumTF.requestFocus();
            }
            String deleteAccounts = accountNumTF.getText();
            Account findAccount;
            if (!Validator.isText(deleteAccounts)) {
                fieldError(accountNumTF, "계좌번호를 입력하세요.");
                return;
            } else {
                findAccount = accountService.findAccount(deleteAccounts);
            }
            if (findAccount != null && findAccount.getAccountNum().equals(deleteAccounts)) {
                listTA.setText("");
                printHeader();
                accountService.removeAccount(deleteAccounts);
                JOptionPane.showMessageDialog(null,"※삭제가 완료 되었습니다.","안내창",JOptionPane.INFORMATION_MESSAGE);
            }else {
                showMessage("계좌번호를 확인해주세요.");
            }
        }

    /**
     * 모든 텍스트 필드를 초기화하는 기능 처리
     */
            private void setClearField () {
                Component[] components = getComponents();
                for (Component component : components) {
                    if (component instanceof TextField) {
                        ((TextField) component).setText("");
                    }
                }
                accountNumTF.requestFocus();
         }
    /**
     * 프로그램의 종료
     * 안전한 프로그램 종료를 위해 사용자에게 다시 한번 묻는 창 생성
     */
    private void exit(){
        int confirm = JOptionPane.showConfirmDialog(null,"프로그램을 종료합니다","종료확인",JOptionPane.YES_NO_OPTION,JOptionPane.INFORMATION_MESSAGE);
        if(confirm == JOptionPane.OK_OPTION){
            /**
             * OS로 부터 빌려온 리소스 반납하는 과정 삽입
             */
            dispose();
            setVisible(false);
            System.exit(0);
      }
    }
  }
