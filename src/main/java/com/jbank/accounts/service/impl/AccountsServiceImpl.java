package com.jbank.accounts.service.impl;

import com.jbank.accounts.constants.AccountsConstants;
import com.jbank.accounts.dto.AccountsDto;
import com.jbank.accounts.dto.CustomerDto;
import com.jbank.accounts.entity.Accounts;
import com.jbank.accounts.entity.Customer;
import com.jbank.accounts.exception.CustomerAlreadyExistsException;
import com.jbank.accounts.exception.ResourceNotFoundException;
import com.jbank.accounts.mapper.AccountsMapper;
import com.jbank.accounts.mapper.CustomerMapper;
import com.jbank.accounts.repository.AccountsRepository;
import com.jbank.accounts.repository.CustomerRepository;
import com.jbank.accounts.service.IAccountsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class AccountsServiceImpl implements IAccountsService {

    private AccountsRepository accountsRepository;
    private CustomerRepository customerRepository;

    /**
     * Create a new account based on the information given in the
     * {@code customerDto}.
     *
     * @param customerDto the customer information to create the account from
     */
    @Override
    public void createAccount(CustomerDto customerDto) {
        Customer customer = CustomerMapper.mapToCustomer(customerDto, new Customer());
        Optional<Customer> optionalCustomer = customerRepository.findByMobileNumber(customerDto.getMobileNumber());
        if (optionalCustomer.isPresent()) {
            throw new CustomerAlreadyExistsException("Customer already exists with mobile number " + customerDto.getMobileNumber());
        }
        Customer savedCustomer = customerRepository.save(customer);
        accountsRepository.save(createNewAccount(savedCustomer));
    }

    /**
     * Fetch account details for a customer using their mobile number.
     *
     * @param mobileNumber the mobile number of the customer whose account details are to be fetched
     * @return a {@link CustomerDto} with the customer and account details
     * @throws ResourceNotFoundException if the customer or account is not found
     */
    @Override
    public CustomerDto fetchAccount(String mobileNumber) {
        Customer customer =customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer" , "mobileNumber", mobileNumber));
        Accounts account = accountsRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(
                () -> new ResourceNotFoundException("Account", "customerId", customer.getCustomerId().toString()));
        CustomerDto customerDto = CustomerMapper.mapToCustomerDto(customer, new CustomerDto());
        customerDto.setAccountsDto(AccountsMapper.mapToAccountsDto(account, new AccountsDto()));
        return customerDto;
    }

/**
 * Update the account and customer details based on the information provided
 * in the {@code customerDto}.
 *
 * @param customerDto the customer and account information to update
 * @return true if the account and customer details are updated successfully, false otherwise
 * @throws ResourceNotFoundException if the account or customer is not found
 */
    @Override
    public boolean updateAccount(CustomerDto customerDto) {
        boolean isUpdated = false;
        AccountsDto accountsDto = customerDto.getAccountsDto();
        if (accountsDto != null) {
            Accounts account = accountsRepository.findById(accountsDto.getAccountNumber()).orElseThrow(
                    () -> new ResourceNotFoundException("Account", "accountNumber", accountsDto.getAccountNumber().toString()));
            AccountsMapper.mapToAccounts(accountsDto, account);
            account = accountsRepository.save(account);

            Long customerId = account.getCustomerId();
            Customer customer = customerRepository.findById(customerId).orElseThrow(
                    () -> new ResourceNotFoundException("Customer", "CustomerId", customerId.toString()));
            CustomerMapper.mapToCustomer(customerDto, customer);
            customerRepository.save(customer);
            isUpdated = true;
        }
        return isUpdated;
    }

    /**
     * Delete a customer's account based on the given mobile number.
     *
     * @param mobileNumber the mobile number of the customer whose account is to be deleted
     * @return true if the account is deleted, false otherwise
     * @throws ResourceNotFoundException if the customer or account is not found
     */
    @Override
    public boolean deleteAccount(String mobileNumber) {
        boolean isDeleted = false;
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber));
        accountsRepository.deleteByCustomerId(customer.getCustomerId());
        customerRepository.deleteById(customer.getCustomerId());
        isDeleted = true;
        return isDeleted;
    }


    /**
     * Create a new {@link Accounts} object using the given customer
     *
     * @param customer the customer for which the account is to be created
     * @return a new {@link Accounts} object
     */
    private Accounts createNewAccount(Customer customer) {
        Accounts newAccount = new Accounts();
        newAccount.setCustomerId(customer.getCustomerId());
        long randomAccountNumber = 1000000000L + new Random().nextInt(900000000);

        newAccount.setAccountNumber(randomAccountNumber);
        newAccount.setAccountType(AccountsConstants.SAVINGS);
        newAccount.setBranchAddress(AccountsConstants.ADDRESS);
        return newAccount;
    }
}
