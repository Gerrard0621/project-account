package com.ezen.ams.model;

import com.ezen.ams.exception.InsufficientException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TreeMap;

/**
 * TreeMap 콜렉션을 이용한 계좌목록 관리
 */
public class AccountService {

    private TreeMap<String, Account> accounts;

    public AccountService(){
        accounts = new TreeMap<>();
    }

    public Collection<Account> getAccounts() {
        return accounts.values();
    }

    public int getCount(){
        return accounts.size();
    }


    /** 
     * 새로운 계좌 생성
     * @param account 생성하고자 하는 계좌의 정보 등록
     */
    public void addAccount(Account account) {
       accounts.put(account.getAccountNum(), account);
    }

    /**
     * 계좌에 돈을 입금하는 기능 처리
     * @param accountNum 입금하고자 하는 계좌번호
     * @param money 입금하고자 하는 금액
     * @return 예금 입금
     * @throws InsufficientException 예외가 발생이 되면 이유와 코드(임의 번호) 출력
     */
    public long depositAccount(String accountNum, long money) throws InsufficientException {
        if(money < 1000){
            throw new InsufficientException("입금하고자 하는 금액이 1,000원 미만이거나 음수일 수 없습니다.", 100);
        }

        Account findAccount = accounts.get(accountNum);
        if(findAccount == null){
            throw new InsufficientException("입금하고자 하는 계좌가 존재하지 않습니다.", 200);
        }
        return findAccount.deposit(money);
    }

    /**
     * 예금을 출금하는 기능 처리
     * @param accountNum 출금하고자 하는 계좌번호 
     * @param money 출금하고자 하는 금액
     * @return 예금 출금
     * @throws InsufficientException 예외가 발생이 되면 이유와 코드(임의 번호) 출력
     */
    public long withdrawAccount(String accountNum, long money) throws InsufficientException{
        if(money < 1000){
            throw new InsufficientException("출금하고자 하는 금액이 1,000원 미만이거나 음수일 수 없습니다.", 110);
        }
        
        Account findAccount = accounts.get(accountNum);
        if(findAccount == null){
            // 비즈니즈 예외 발생했기 때문에 강제 예외 발생
            throw new InsufficientException("출금하고자 하는 계좌가 존재하지 않습니다.", 200);
        }

        if(findAccount.getBalance() < money){
            throw new InsufficientException("출금 잔액이 부족합니다.", 230);

        }
        return findAccount.withdraw(money);
    }

    /**
     * 계좌번호로 계좌 검색하는 기능 처리
     * @param accountNum 검색하고자 하는 계좌번호 입력
     * @return 조회 된 계좌번호
     */
    public Account findAccount(String accountNum) {
        return accounts.get(accountNum);
    }

    /**
     * 예금주 이름으로 계좌 검색하는 기능 처리 
     * @param accountOwner 검색하고자 하는 예금주 이름 입력
     * @return 찾고자 하는 계좌번호
     */
    public List<Account> findAccountByAccountOwner(String accountOwner){
        List<Account> findAccounts = new ArrayList<>();
        Collection<Account> values = accounts.values();
        for (Account account : values) {
            String owner = account.getAccountOwner();
            if(owner.equals(accountOwner)) {
                findAccounts.add(account);
            }
        }
        return findAccounts;
    }

    /**
     * 선택 된 계좌를 삭제하는 기능 처리
     * @param accountNum 삭제하고자 하는 계좌번호 입력
     * @return 계좌 삭제
     */
    public boolean removeAccount(String accountNum) {
        Account account = accounts.remove(accountNum);
        if(account != null){
            return true;
        }
        return false;
    }

    //테스트를 위한 main클래스

    public static void main(String[] args) {
        AccountService accountService = new AccountService();
        accountService.addAccount(new Account("1111-2222","신연재",1111,10000));
        accountService.addAccount(new Account("1111-3333","연재",1111,20000));
        accountService.addAccount(new MinusAccount("2222-3333","홍길동",2222,10000,50000));

        Collection<Account> accounts1 = accountService.getAccounts();
        if (!accounts1.isEmpty()){
            for (Account account : accounts1) {
                System.out.println(account);
            }
        }
       Account findAccount1 = accountService.findAccount("111-2222");
        if (!(findAccount1 == null)){
            System.out.println("검색된 계좌 : "+ findAccount1);
        }else {
            System.out.println("X");
        }
    }
}