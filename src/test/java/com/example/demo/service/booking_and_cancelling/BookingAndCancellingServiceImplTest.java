package com.example.demo.service.booking_and_cancelling;

import com.example.demo.dto.CancelDTO;
import com.example.demo.dto.PassengerDTO;
import com.example.demo.exception.BankDetailsException;
import com.example.demo.exception.InvalidDetailsException;
import com.example.demo.exception.NoTicketsAvailableException;
import com.example.demo.model.Account;
import com.example.demo.model.Passenger;
import com.example.demo.model.Payment;
import com.example.demo.model.Train;
import com.example.demo.service.account_service.NorthAccountService;
import com.example.demo.service.email_service.EmailService;
import com.example.demo.service.passImpl.NorthPassengerService;
import com.example.demo.service.passImpl.PassengerService;
import com.example.demo.service.passImpl.SouthPassengerService;
import com.example.demo.service.paymentImpl.NorthPaymentService;
import com.example.demo.service.trainImpl.NorthIndianTrainServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


class BookingAndCancellingServiceImplTest {


    @InjectMocks
    private BookingAndCancellingServiceImpl bookingAndCancellingService;
    @Mock
    private EmailService emailService;
    @Mock
    private NorthIndianTrainServiceImpl northIndianTrainService;
    @Mock
    private NorthPassengerService northPassengerService;
    @Mock
    private NorthPaymentService northPaymentService;

    @Mock
    private NorthAccountService northAccountService;

    @Mock
    private SouthPassengerService southPassengerService;
    //repo
    @Mock
    private JavaMailSender mailSender;



    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void cancelBookingForPassenger_Success_rac_and_wl_has_no_passengers(){
        CancelDTO cancelDTO = new CancelDTO();
        cancelDTO.setTrainId("66307e8c84580e7584542e18");
        cancelDTO.setPassId("66307e8c84580e7584542e16");
        cancelDTO.setFrom("Delhi");
        cancelDTO.setTo("Chennai");
        cancelDTO.setBerth("L");
        cancelDTO.setEmailId("abdulaziz2002@gmail.com");


        Train train = new Train();
        train.setId("66307e8c84580e7584542e18");
        train.setTrainName("Delhi Express");
        train.setDate("14/04/24");
        train.setFrom("Delhi");
        train.setTo("Chennai");
        train.setUpperBerthCount(0);
        train.setLowerBerthCount(1);
        train.setMiddleBerthCount(0);
        train.setRacCount(0);
        train.setWaitingCount(0);
        train.setHomeRegion("NR");
        train.setFromRegion("NR");
        train.setToRegion("SR");
        train.setTrainFee(456.99);

        Account account = new Account();
        account.setId("6630805684580e7584542e1a");
        account.setAccountNumber("123456789098765");
        account.setAccountHolderName("SAMSON");
        account.setCVV("987");
        account.setBalance(1000.8);

        Passenger passengerToBeRemoved = new Passenger();
        passengerToBeRemoved.setId("66307e8c84580e7584542e16");
        passengerToBeRemoved.setPassengerTrainId("66307e8c84580e7584542e18");
        passengerToBeRemoved.setPassengerName("Samson");
        passengerToBeRemoved.setAllowedPreference("L");
        passengerToBeRemoved.setSeatNumber(1);
        passengerToBeRemoved.setFrom("Delhi");
        passengerToBeRemoved.setTo("Chennai");
        passengerToBeRemoved.setPassengerTrainName("Delhi Express");
        passengerToBeRemoved.setBill(new Payment("Delhi Express","66307e8c84580e7584542e18","Samson","66307e8c84580e7584542e16","L",1,450.99));
        passengerToBeRemoved.setAccount(account);


        when(northIndianTrainService.findTrainById(any())).thenReturn(Optional.of(train));
        when(northPassengerService.findPassengerById(any())).thenReturn(Optional.of(passengerToBeRemoved));


        String cancelledMessage = bookingAndCancellingService.cancelBookingForPassenger(cancelDTO);
        assertNotNull(cancelledMessage);
        verify(northPassengerService,times(1)).delete(passengerToBeRemoved);
        verify(southPassengerService,times(1)).delete(passengerToBeRemoved);
        verify(northIndianTrainService,times(1)).saveTrain(train);
        verify(northAccountService,times(1)).saveAccount(account);
    }


    @Test
    void cancelBookingForPassenger_Success_rac_and_wl_has_passengers() {
        //Given
        CancelDTO cancelDTO = new CancelDTO();
        cancelDTO.setTrainId("66307e8c84580e7584542e18");
        cancelDTO.setPassId("66307e8c84580e7584542e16");
        cancelDTO.setFrom("Delhi");
        cancelDTO.setTo("Chennai");
        cancelDTO.setBerth("L");
        cancelDTO.setEmailId("abdulaziz2002@gmail.com");

        Train train = new Train();
        train.setId("66307e8c84580e7584542e18");
        train.setTrainName("Delhi Express");
        train.setDate("14/04/24");
        train.setFrom("Delhi");
        train.setTo("Chennai");
        train.setUpperBerthCount(1);
        train.setLowerBerthCount(1);
        train.setMiddleBerthCount(1);
        train.setHomeRegion("NR");
        train.setFromRegion("NR");
        train.setToRegion("SR");
        train.setTrainFee(456.99);

        Account account1 = new Account();
        account1.setId("6630805684580e7584542e1a");
        account1.setAccountNumber("123456789098765");
        account1.setAccountHolderName("SAMSON");
        account1.setCVV("987");
        account1.setBalance(1000.8);

        Account account2 = new Account();
        account2.setId("6630805684580e7584542e1a");
        account2.setAccountNumber("123456789098765");
        account2.setAccountHolderName("SAMSON");
        account2.setCVV("987");
        account2.setBalance(1000.8);

        Account account3 = new Account();
        account3.setId("6630805684580e7584542e1a");
        account3.setAccountNumber("123456789098765");
        account3.setAccountHolderName("SAMSON");
        account3.setCVV("987");
        account3.setBalance(1000.8);

        //Passenger To be removed

        Passenger passengerToBeRemoved = new Passenger();
        passengerToBeRemoved.setId("66307e8c84580e7584542e16");
        passengerToBeRemoved.setPassengerTrainId("66307e8c84580e7584542e18");
        passengerToBeRemoved.setPassengerName("Samson");
        passengerToBeRemoved.setAllowedPreference("L");
        passengerToBeRemoved.setSeatNumber(1);
        passengerToBeRemoved.setFrom("Delhi");
        passengerToBeRemoved.setTo("Chennai");
        passengerToBeRemoved.setPassengerTrainName("Delhi Express");
        passengerToBeRemoved.setBill(new Payment("Delhi Express","66307e8c84580e7584542e18","Samson","66307e8c84580e7584542e16","L",1,450.99));
        passengerToBeRemoved.setAccount(account1);


        Passenger passengerToBeRemovedRAC = new Passenger();
        passengerToBeRemovedRAC.setId("66307e8c84580e7584542e19");
        passengerToBeRemovedRAC.setPassengerTrainId("66307e8c84580e7584542e18");
        passengerToBeRemovedRAC.setPassengerName("Dhoni");
        passengerToBeRemovedRAC.setAllowedPreference("RAC");
        passengerToBeRemovedRAC.setSeatNumber(1);
        passengerToBeRemovedRAC.setFrom("Delhi");
        passengerToBeRemovedRAC.setTo("Chennai");
        passengerToBeRemovedRAC.setPassengerTrainName("Delhi Express");
        passengerToBeRemovedRAC.setBill(new Payment("Delhi Express","66307e8c84580e7584542e18","Dhoni","66307e8c84580e7584542e19","RAC",1,450.99));
        passengerToBeRemovedRAC.setAccount(account2);

        Passenger passengerToBeRemovedWL = new Passenger();
        passengerToBeRemovedWL.setId("66307e8c84580e7584542e20");
        passengerToBeRemovedWL.setPassengerTrainId("66307e8c84580e7584542e18");
        passengerToBeRemovedWL.setPassengerName("Kholi");
        passengerToBeRemovedWL.setAllowedPreference("WL");
        passengerToBeRemovedWL.setSeatNumber(1);
        passengerToBeRemovedWL.setFrom("Delhi");
        passengerToBeRemovedWL.setTo("Chennai");
        passengerToBeRemovedWL.setPassengerTrainName("Delhi Express");
        passengerToBeRemovedWL.setBill(new Payment("Delhi Express","66307e8c84580e7584542e18","Kholi","66307e8c84580e7584542e20","Wl",1,450.99));
        passengerToBeRemovedWL.setAccount(account3);

        train.getRacPassengers().add(passengerToBeRemovedRAC);
        train.getWaitingList().add(passengerToBeRemovedWL);
        train.getLowerBerth().add(passengerToBeRemoved);

        when(northIndianTrainService.findTrainById(any())).thenReturn(Optional.of(train));

        when(northPassengerService.findPassengerById(any())).thenReturn(Optional.of(passengerToBeRemoved));

        when(southPassengerService.findPassengerById(any())).thenReturn(Optional.of(passengerToBeRemoved));
        // <>
        doNothing().when(southPassengerService).delete(passengerToBeRemoved);

        String cancelMessage = bookingAndCancellingService.cancelBookingForPassenger(cancelDTO);


        verify(southPassengerService,times(1)).delete(passengerToBeRemoved);
        verify(northPassengerService,times(1)).savePassenger(passengerToBeRemovedRAC);
        verify(northPaymentService,times(1)).savePayment(passengerToBeRemovedRAC.getBill());
        verify(southPassengerService,times(1)).savePassenger(passengerToBeRemovedRAC);
        verify(northPaymentService,times(1)).delete(passengerToBeRemoved.getBill());
        verify(northPassengerService,times(1)).delete(passengerToBeRemoved);
        verify(northPaymentService,times(1)).savePayment(passengerToBeRemovedWL.getBill());
        verify(northIndianTrainService,times(1)).saveTrain(train);
        verify(northAccountService,times(1)).saveAccount(any());
    }


    @Test
    void cancelBookingForPassenger_Failure_TrainId_is_null() {
        //Given
        CancelDTO cancelDTO = new CancelDTO();
        cancelDTO.setTrainId(null);
        //When
        InvalidDetailsException invalidDetailsException = assertThrows(InvalidDetailsException.class, () -> {
            bookingAndCancellingService.cancelBookingForPassenger(cancelDTO);
        });
        assertEquals("Train ID is null", invalidDetailsException.getMessage());
        verifyNoInteractions(northIndianTrainService);
        verifyNoInteractions(northPassengerService);
        verifyNoInteractions(northPaymentService);
        verifyNoInteractions(southPassengerService);
        verifyNoInteractions(emailService);
    }


    @Test
    void cancelBookingForPassenger_Failure_TrainId_is_empty() {
        //Given
        CancelDTO cancelDTO = new CancelDTO();
        cancelDTO.setTrainId("");
        //When
        InvalidDetailsException invalidDetailsException = assertThrows(InvalidDetailsException.class, () -> {
            bookingAndCancellingService.cancelBookingForPassenger(cancelDTO);
        });
        assertEquals("Train ID is empty", invalidDetailsException.getMessage());
        verifyNoInteractions(northIndianTrainService);
        verifyNoInteractions(northPassengerService);
        verifyNoInteractions(northPaymentService);
        verifyNoInteractions(southPassengerService);
        verifyNoInteractions(emailService);
    }

    @Test
    void cancelBookingForPassenger_Failure_PassengerId_is_null() {
        CancelDTO cancelDTO = new CancelDTO();
        cancelDTO.setTrainId("66307e8c84580e7584542e19");
        cancelDTO.setPassId(null);
        InvalidDetailsException invalidDetailsException = assertThrows(InvalidDetailsException.class, () -> {
            bookingAndCancellingService.cancelBookingForPassenger(cancelDTO);
        });
        assertEquals("Pass ID is null", invalidDetailsException.getMessage());
        verifyNoInteractions(northIndianTrainService);
        verifyNoInteractions(northPassengerService);
        verifyNoInteractions(northPaymentService);
        verifyNoInteractions(southPassengerService);
        verifyNoInteractions(emailService);
    }

    @Test
    void cancelBookingForPassenger_Failure_PassengerId_is_empty() {
        CancelDTO cancelDTO = new CancelDTO();
        cancelDTO.setTrainId("66307e8c84580e7584542e19");
        cancelDTO.setPassId("");
        InvalidDetailsException invalidDetailsException = assertThrows(InvalidDetailsException.class, () -> {
            bookingAndCancellingService.cancelBookingForPassenger(cancelDTO);
        });
        assertEquals("Pass ID is empty", invalidDetailsException.getMessage());
        verifyNoInteractions(northIndianTrainService);
        verifyNoInteractions(northPassengerService);
        verifyNoInteractions(northPaymentService);
        verifyNoInteractions(southPassengerService);
        verifyNoInteractions(emailService);
    }

    @Test
    void cancelBookingForPassenger_Failure_InvalidBerthPreference() {
        CancelDTO cancelDTO = new CancelDTO();
        cancelDTO.setTrainId("66307e8c84580e7584542e19");
        cancelDTO.setPassId("66307e8c84580e7584542e12");
        cancelDTO.setBerth("D");

        InvalidDetailsException invalidDetailsException = assertThrows(InvalidDetailsException.class, () -> {
            bookingAndCancellingService.cancelBookingForPassenger(cancelDTO);
        });

        assertEquals("Berth preference is invalid", invalidDetailsException.getMessage());
        verifyNoInteractions(northIndianTrainService);
        verifyNoInteractions(northPassengerService);
        verifyNoInteractions(northPaymentService);
        verifyNoInteractions(southPassengerService);
        verifyNoInteractions(emailService);
    }

    @Test
    void cancelBookingForPassenger_Failure_BerthPreference_is_null() {
        CancelDTO cancelDTO = new CancelDTO();
        cancelDTO.setTrainId("66307e8c84580e7584542e19");
        cancelDTO.setPassId("66307e8c84580e7584542e12");
        cancelDTO.setBerth(null);

        InvalidDetailsException invalidDetailsException = assertThrows(InvalidDetailsException.class, () -> {
            bookingAndCancellingService.cancelBookingForPassenger(cancelDTO);
        });

        assertEquals("Berth is null", invalidDetailsException.getMessage());
        verifyNoInteractions(northIndianTrainService);
        verifyNoInteractions(northPassengerService);
        verifyNoInteractions(northPaymentService);
        verifyNoInteractions(southPassengerService);
        verifyNoInteractions(emailService);
    }

    @Test
    void cancelBookingForPassenger_Failure_BerthPreference_is_empty() {
        CancelDTO cancelDTO = new CancelDTO();
        cancelDTO.setTrainId("66307e8c84580e7584542e19");
        cancelDTO.setPassId("66307e8c84580e7584542e12");
        cancelDTO.setBerth("");

        InvalidDetailsException invalidDetailsException = assertThrows(InvalidDetailsException.class, () -> {
            bookingAndCancellingService.cancelBookingForPassenger(cancelDTO);
        });

        assertEquals("Berth is empty", invalidDetailsException.getMessage());
        verifyNoInteractions(northIndianTrainService);
        verifyNoInteractions(northPassengerService);
        verifyNoInteractions(northPaymentService);
        verifyNoInteractions(southPassengerService);
        verifyNoInteractions(emailService);
    }


    @Test
    void cancelBookingForPassenger_Failure_From_Details_is_null(){
        CancelDTO cancelDTO = new CancelDTO();
        cancelDTO.setTrainId("66307e8c84580e7584542e19");
        cancelDTO.setPassId("66307e8c84580e7584542e12");
        cancelDTO.setBerth("L");
        cancelDTO.setFrom(null);

        InvalidDetailsException invalidDetailsException = assertThrows(InvalidDetailsException.class, () -> {
           bookingAndCancellingService.cancelBookingForPassenger(cancelDTO);
        });


        assertEquals("from details is null", invalidDetailsException.getMessage());
        verifyNoInteractions(northIndianTrainService);
        verifyNoInteractions(northPassengerService);
        verifyNoInteractions(northPaymentService);
        verifyNoInteractions(southPassengerService);
        verifyNoInteractions(emailService);
    }

    @Test
    void cancelBookingForPassenger_Failure_From_Details_is_empty(){
        CancelDTO cancelDTO = new CancelDTO();
        cancelDTO.setTrainId("66307e8c84580e7584542e19");
        cancelDTO.setPassId("66307e8c84580e7584542e12");
        cancelDTO.setBerth("L");
        cancelDTO.setFrom("");

        InvalidDetailsException invalidDetailsException = assertThrows(InvalidDetailsException.class, () -> {
            bookingAndCancellingService.cancelBookingForPassenger(cancelDTO);
        });


        assertEquals("from details is empty", invalidDetailsException.getMessage());
        verifyNoInteractions(northIndianTrainService);
        verifyNoInteractions(northPassengerService);
        verifyNoInteractions(northPaymentService);
        verifyNoInteractions(southPassengerService);
        verifyNoInteractions(emailService);
    }

    @Test
    void cancelBookingForPassenger_Failure_To_Details_is_null(){
        CancelDTO cancelDTO = new CancelDTO();
        cancelDTO.setTrainId("66307e8c84580e7584542e19");
        cancelDTO.setPassId("66307e8c84580e7584542e12");
        cancelDTO.setBerth("L");
        cancelDTO.setFrom("Delhi");
        cancelDTO.setTo(null);

        InvalidDetailsException invalidDetailsException = assertThrows(InvalidDetailsException.class, () -> {
            bookingAndCancellingService.cancelBookingForPassenger(cancelDTO);
        });


        assertEquals("to details is null", invalidDetailsException.getMessage());
        verifyNoInteractions(northIndianTrainService);
        verifyNoInteractions(northPassengerService);
        verifyNoInteractions(northPaymentService);
        verifyNoInteractions(southPassengerService);
        verifyNoInteractions(emailService);
    }

    @Test
    void cancelBookingForPassenger_Failure_To_Details_is_empty(){
        CancelDTO cancelDTO = new CancelDTO();
        cancelDTO.setTrainId("66307e8c84580e7584542e19");
        cancelDTO.setPassId("66307e8c84580e7584542e12");
        cancelDTO.setBerth("L");
        cancelDTO.setFrom("Delhi");
        cancelDTO.setTo("");

        InvalidDetailsException invalidDetailsException = assertThrows(InvalidDetailsException.class, () -> {
            bookingAndCancellingService.cancelBookingForPassenger(cancelDTO);
        });


        assertEquals("to details is empty", invalidDetailsException.getMessage());
        verifyNoInteractions(northIndianTrainService);
        verifyNoInteractions(northPassengerService);
        verifyNoInteractions(northPaymentService);
        verifyNoInteractions(southPassengerService);
        verifyNoInteractions(emailService);
    }


    //success cases

    @Test
    void bookTicketForPassenger_Success_for_lower_berth() {
        Train train = new Train();
        train.setId("6630805684580e7584542e1a");
        train.setTrainName("Delhi Express");
        train.setDate("14/04/24");
        train.setFrom("Delhi");
        train.setTo("Chennai");
        train.setUpperBerthCount(0);
        train.setLowerBerthCount(1);
        train.setMiddleBerthCount(0);
        train.setHomeRegion("NR");
        train.setFromRegion("NR");
        train.setToRegion("SR");
        train.setTrainFee(456.99);


        Account account = new Account();
        account.setId("6630805684580e7584542e1a");
        account.setAccountNumber("123456789098765");
        account.setAccountHolderName("SAMSON");
        account.setCVV("987");
        account.setBalance(1000.8);

        PassengerDTO passengerDTO = new PassengerDTO();
        passengerDTO.setPassengerTrainId("6630805684580e7584542e1a");
        passengerDTO.setPassengerName("Samson");
        passengerDTO.setAge(31);
        passengerDTO.setDate("14/04/24");
        passengerDTO.setFrom("Delhi");
        passengerDTO.setTo("Chennai");
        passengerDTO.setBerthPreference("L");
        passengerDTO.setEmailId("abdulaziz2002ece@gmail.com");
        passengerDTO.setAccount(account);

        when(northAccountService.findAccountById("6630805684580e7584542e1a")).thenReturn(Optional.of(account));
        when(northIndianTrainService.findTrainById("6630805684580e7584542e1a")).thenReturn(Optional.of(train));

        Passenger bookedPassenger = bookingAndCancellingService.bookPassenger(passengerDTO);

        assertNotNull(bookedPassenger);
        assertEquals("L",bookedPassenger.getAllowedPreference());
        verify(northAccountService,times(1)).addAccount(any());
        verify(northPassengerService,times(2)).savePassenger(any());
        verify(northPaymentService,times(1)).savePayment(any());
        verify(northIndianTrainService,times(1)).updateTrain(any());
        verify(southPassengerService,times(1)).savePassenger(any());
        verify(emailService,times(1)).sentEmail(anyString(),anyString(),anyString());
    }

    @Test
    void bookTicketForPassenger_Success_for_upper_berth() {
        Train train = new Train();
        train.setId("6630805684580e7584542e1a");
        train.setTrainName("Delhi Express");
        train.setDate("14/04/24");
        train.setFrom("Delhi");
        train.setTo("Chennai");
        train.setUpperBerthCount(1);
        train.setLowerBerthCount(0);
        train.setMiddleBerthCount(0);
        train.setHomeRegion("NR");
        train.setFromRegion("NR");
        train.setToRegion("SR");
        train.setTrainFee(456.99);


        Account account = new Account();
        account.setId("6630805684580e7584542e1a");
        account.setAccountNumber("123456789098765");
        account.setAccountHolderName("SAMSON");
        account.setCVV("987");
        account.setBalance(1000.8);

        PassengerDTO passengerDTO = new PassengerDTO();
        passengerDTO.setPassengerTrainId("6630805684580e7584542e1a");
        passengerDTO.setPassengerName("Samson");
        passengerDTO.setAge(31);
        passengerDTO.setDate("14/04/24");
        passengerDTO.setFrom("Delhi");
        passengerDTO.setTo("Chennai");
        passengerDTO.setBerthPreference("U");
        passengerDTO.setEmailId("abdulaziz2002ece@gmail.com");
        passengerDTO.setAccount(account);

        when(northAccountService.findAccountById("6630805684580e7584542e1a")).thenReturn(Optional.of(account));
        when(northIndianTrainService.findTrainById("6630805684580e7584542e1a")).thenReturn(Optional.of(train));

        Passenger bookedPassenger = bookingAndCancellingService.bookPassenger(passengerDTO);

        assertNotNull(bookedPassenger);
        assertEquals("U",bookedPassenger.getAllowedPreference());
        verify(northPassengerService,times(2)).savePassenger(any());
        verify(northPaymentService,times(1)).savePayment(any());
        verify(northIndianTrainService,times(1)).updateTrain(any());
        verify(southPassengerService,times(1)).savePassenger(any());
        verify(emailService,times(1)).sentEmail(anyString(),anyString(),anyString());
    }

    @Test
    void bookTicketForPassenger_Success_for_middle_berth() {
        Train train = new Train();
        train.setId("6630805684580e7584542e1a");
        train.setTrainName("Delhi Express");
        train.setDate("14/04/24");
        train.setFrom("Delhi");
        train.setTo("Chennai");
        train.setUpperBerthCount(0);
        train.setLowerBerthCount(0);
        train.setMiddleBerthCount(1);
        train.setHomeRegion("NR");
        train.setFromRegion("NR");
        train.setToRegion("SR");
        train.setTrainFee(456.99);

        Account account = new Account();
        account.setId("6630805684580e7584542e1a");
        account.setAccountNumber("123456789098765");
        account.setAccountHolderName("SAMSON");
        account.setCVV("987");
        account.setBalance(1000.8);

        PassengerDTO passengerDTO = new PassengerDTO();
        passengerDTO.setPassengerTrainId("6630805684580e7584542e1a");
        passengerDTO.setPassengerName("Samson");
        passengerDTO.setAge(31);
        passengerDTO.setDate("14/04/24");
        passengerDTO.setFrom("Delhi");
        passengerDTO.setTo("Chennai");
        passengerDTO.setBerthPreference("M");
        passengerDTO.setEmailId("abdulaziz2002ece@gmail.com");
        passengerDTO.setAccount(account);

        when(northAccountService.findAccountById("6630805684580e7584542e1a")).thenReturn(Optional.of(account));
        when(northIndianTrainService.findTrainById("6630805684580e7584542e1a")).thenReturn(Optional.of(train));

        Passenger bookedPassenger = bookingAndCancellingService.bookPassenger(passengerDTO);

        assertNotNull(bookedPassenger);
        assertEquals("M",bookedPassenger.getAllowedPreference());
        verify(northPassengerService,times(2)).savePassenger(any());
        verify(northPaymentService,times(1)).savePayment(any());
        verify(northIndianTrainService,times(1)).updateTrain(any());
        verify(southPassengerService,times(1)).savePassenger(any());
        verify(emailService,times(1)).sentEmail(anyString(),anyString(),anyString());
    }


    @Test
    void bookTicketForPassenger_Success_asked_for_lower_berth_got_rac() {
        Train train = new Train();
        train.setId("6630805684580e7584542e1a");
        train.setTrainName("Delhi Express");
        train.setDate("14/04/24");
        train.setFrom("Delhi");
        train.setTo("Chennai");
        train.setUpperBerthCount(0);
        train.setLowerBerthCount(0);
        train.setMiddleBerthCount(0);
        train.setRacCount(1);
        train.setWaitingCount(0);
        train.setHomeRegion("NR");
        train.setFromRegion("NR");
        train.setToRegion("SR");
        train.setTrainFee(456.99);


        Account account = new Account();
        account.setId("6630805684580e7584542e1a");
        account.setAccountNumber("123456789098765");
        account.setAccountHolderName("SAMSON");
        account.setCVV("987");
        account.setBalance(1000.8);

        PassengerDTO passengerDTO = new PassengerDTO();
        passengerDTO.setPassengerTrainId("6630805684580e7584542e1a");
        passengerDTO.setPassengerName("Samson");
        passengerDTO.setAge(31);
        passengerDTO.setDate("14/04/24");
        passengerDTO.setFrom("Delhi");
        passengerDTO.setTo("Chennai");
        passengerDTO.setBerthPreference("L");
        passengerDTO.setEmailId("abdulaziz2002ece@gmail.com");
        passengerDTO.setAccount(account);

        when(northAccountService.findAccountById("6630805684580e7584542e1a")).thenReturn(Optional.of(account));
        when(northIndianTrainService.findTrainById("6630805684580e7584542e1a")).thenReturn(Optional.of(train));

        Passenger bookedPassenger = bookingAndCancellingService.bookPassenger(passengerDTO);

        assertNotNull(bookedPassenger);
        assertEquals("RAC",bookedPassenger.getAllowedPreference());
        verify(northPassengerService,times(2)).savePassenger(any());
        verify(northPaymentService,times(1)).savePayment(any());
        verify(northIndianTrainService,times(1)).updateTrain(any());
        verify(southPassengerService,times(1)).savePassenger(any());
        verify(emailService,times(1)).sentEmail(anyString(),anyString(),anyString());
    }

    @Test
    void bookTicketForPassenger_Success_asked_for_middle_berth_got_rac() {
        Train train = new Train();
        train.setId("6630805684580e7584542e1a");
        train.setTrainName("Delhi Express");
        train.setDate("14/04/24");
        train.setFrom("Delhi");
        train.setTo("Chennai");
        train.setUpperBerthCount(0);
        train.setLowerBerthCount(0);
        train.setMiddleBerthCount(0);
        train.setRacCount(1);
        train.setWaitingCount(0);
        train.setHomeRegion("NR");
        train.setFromRegion("NR");
        train.setToRegion("SR");
        train.setTrainFee(456.99);


        Account account = new Account();
        account.setId("6630805684580e7584542e1a");
        account.setAccountNumber("123456789098765");
        account.setAccountHolderName("SAMSON");
        account.setCVV("987");
        account.setBalance(1000.8);

        PassengerDTO passengerDTO = new PassengerDTO();
        passengerDTO.setPassengerTrainId("6630805684580e7584542e1a");
        passengerDTO.setPassengerName("Samson");
        passengerDTO.setAge(31);
        passengerDTO.setDate("14/04/24");
        passengerDTO.setFrom("Delhi");
        passengerDTO.setTo("Chennai");
        passengerDTO.setBerthPreference("M");
        passengerDTO.setEmailId("abdulaziz2002ece@gmail.com");
        passengerDTO.setAccount(account);

        when(northAccountService.findAccountById("6630805684580e7584542e1a")).thenReturn(Optional.of(account));
        when(northIndianTrainService.findTrainById("6630805684580e7584542e1a")).thenReturn(Optional.of(train));

        Passenger bookedPassenger = bookingAndCancellingService.bookPassenger(passengerDTO);

        assertNotNull(bookedPassenger);
        assertEquals("RAC",bookedPassenger.getAllowedPreference());
        verify(northPassengerService,times(2)).savePassenger(any());
        verify(northPaymentService,times(1)).savePayment(any());
        verify(northIndianTrainService,times(1)).updateTrain(any());
        verify(southPassengerService,times(1)).savePassenger(any());
        verify(emailService,times(1)).sentEmail(anyString(),anyString(),anyString());
    }

    @Test
    void bookTicketForPassenger_Success_asked_for_upper_berth_got_rac() {
        Train train = new Train();
        train.setId("6630805684580e7584542e1a");
        train.setTrainName("Delhi Express");
        train.setDate("14/04/24");
        train.setFrom("Delhi");
        train.setTo("Chennai");
        train.setUpperBerthCount(0);
        train.setLowerBerthCount(0);
        train.setMiddleBerthCount(0);
        train.setRacCount(1);
        train.setWaitingCount(0);
        train.setHomeRegion("NR");
        train.setFromRegion("NR");
        train.setToRegion("SR");
        train.setTrainFee(456.99);

        Account account = new Account();
        account.setId("6630805684580e7584542e1a");
        account.setAccountNumber("123456789098765");
        account.setAccountHolderName("SAMSON");
        account.setCVV("987");
        account.setBalance(1000.8);

        PassengerDTO passengerDTO = new PassengerDTO();
        passengerDTO.setPassengerTrainId("6630805684580e7584542e1a");
        passengerDTO.setPassengerName("Samson");
        passengerDTO.setAge(31);
        passengerDTO.setDate("14/04/24");
        passengerDTO.setFrom("Delhi");
        passengerDTO.setTo("Chennai");
        passengerDTO.setBerthPreference("U");
        passengerDTO.setEmailId("abdulaziz2002ece@gmail.com");
        passengerDTO.setAccount(account);

        when(northAccountService.findAccountById("6630805684580e7584542e1a")).thenReturn(Optional.of(account));
        when(northIndianTrainService.findTrainById("6630805684580e7584542e1a")).thenReturn(Optional.of(train));

        Passenger bookedPassenger = bookingAndCancellingService.bookPassenger(passengerDTO);

        assertNotNull(bookedPassenger);
        assertEquals("RAC",bookedPassenger.getAllowedPreference());
        verify(northPassengerService,times(2)).savePassenger(any());
        verify(northPaymentService,times(1)).savePayment(any());
        verify(northIndianTrainService,times(1)).updateTrain(any());
        verify(southPassengerService,times(1)).savePassenger(any());
        verify(emailService,times(1)).sentEmail(anyString(),anyString(),anyString());
    }


    @Test
    void bookTicketForPassenger_Success_asked_for_lower_berth_got_wl() {
        Train train = new Train();
        train.setId("6630805684580e7584542e1a");
        train.setTrainName("Delhi Express");
        train.setDate("14/04/24");
        train.setFrom("Delhi");
        train.setTo("Chennai");
        train.setUpperBerthCount(0);
        train.setLowerBerthCount(0);
        train.setMiddleBerthCount(0);
        train.setRacCount(0);
        train.setWaitingCount(1);
        train.setHomeRegion("NR");
        train.setFromRegion("NR");
        train.setToRegion("SR");
        train.setTrainFee(456.99);

        Account account = new Account();
        account.setId("6630805684580e7584542e1a");
        account.setAccountNumber("123456789098765");
        account.setAccountHolderName("SAMSON");
        account.setCVV("987");
        account.setBalance(1000.8);

        PassengerDTO passengerDTO = new PassengerDTO();
        passengerDTO.setPassengerTrainId("6630805684580e7584542e1a");
        passengerDTO.setPassengerName("Samson");
        passengerDTO.setAge(31);
        passengerDTO.setDate("14/04/24");
        passengerDTO.setFrom("Delhi");
        passengerDTO.setTo("Chennai");
        passengerDTO.setBerthPreference("L");
        passengerDTO.setEmailId("abdulaziz2002ece@gmail.com");
        passengerDTO.setAccount(account);

        when(northAccountService.findAccountById("6630805684580e7584542e1a")).thenReturn(Optional.of(account));
        when(northIndianTrainService.findTrainById("6630805684580e7584542e1a")).thenReturn(Optional.of(train));

        Passenger bookedPassenger = bookingAndCancellingService.bookPassenger(passengerDTO);

        assertNotNull(bookedPassenger);
        assertEquals("WL",bookedPassenger.getAllowedPreference());
        verify(northPassengerService,times(2)).savePassenger(any());
        verify(northPaymentService,times(1)).savePayment(any());
        verify(northIndianTrainService,times(1)).updateTrain(any());
        verify(southPassengerService,times(1)).savePassenger(any());
        verify(emailService,times(1)).sentEmail(anyString(),anyString(),anyString());
    }

    @Test
    void bookTicketForPassenger_Success_asked_for_middle_berth_got_wl() {
        Train train = new Train();
        train.setId("6630805684580e7584542e1a");
        train.setTrainName("Delhi Express");
        train.setDate("14/04/24");
        train.setFrom("Delhi");
        train.setTo("Chennai");
        train.setUpperBerthCount(0);
        train.setLowerBerthCount(0);
        train.setMiddleBerthCount(0);
        train.setRacCount(0);
        train.setWaitingCount(1);
        train.setHomeRegion("NR");
        train.setFromRegion("NR");
        train.setToRegion("SR");
        train.setTrainFee(456.99);

        Account account = new Account();
        account.setId("6630805684580e7584542e1a");
        account.setAccountNumber("123456789098765");
        account.setAccountHolderName("SAMSON");
        account.setCVV("987");
        account.setBalance(1000.8);


        PassengerDTO passengerDTO = new PassengerDTO();
        passengerDTO.setPassengerTrainId("6630805684580e7584542e1a");
        passengerDTO.setPassengerName("Samson");
        passengerDTO.setAge(31);
        passengerDTO.setDate("14/04/24");
        passengerDTO.setFrom("Delhi");
        passengerDTO.setTo("Chennai");
        passengerDTO.setBerthPreference("M");
        passengerDTO.setEmailId("abdulaziz2002ece@gmail.com");
        passengerDTO.setAccount(account);

        when(northAccountService.findAccountById("6630805684580e7584542e1a")).thenReturn(Optional.of(account));
        when(northIndianTrainService.findTrainById("6630805684580e7584542e1a")).thenReturn(Optional.of(train));

        Passenger bookedPassenger = bookingAndCancellingService.bookPassenger(passengerDTO);

        assertNotNull(bookedPassenger);
        assertEquals("WL",bookedPassenger.getAllowedPreference());
        verify(northPassengerService,times(2)).savePassenger(any());
        verify(northPaymentService,times(1)).savePayment(any());
        verify(northIndianTrainService,times(1)).updateTrain(any());
        verify(southPassengerService,times(1)).savePassenger(any());
        verify(emailService,times(1)).sentEmail(anyString(),anyString(),anyString());
    }

    @Test
    void bookTicketForPassenger_Success_asked_for_upper_berth_got_wl() {
        Train train = new Train();
        train.setId("6630805684580e7584542e1a");
        train.setTrainName("Delhi Express");
        train.setDate("14/04/24");
        train.setFrom("Delhi");
        train.setTo("Chennai");
        train.setUpperBerthCount(0);
        train.setLowerBerthCount(0);
        train.setMiddleBerthCount(0);
        train.setRacCount(0);
        train.setWaitingCount(1);
        train.setHomeRegion("NR");
        train.setFromRegion("NR");
        train.setToRegion("SR");
        train.setTrainFee(456.99);

        Account account = new Account();
        account.setId("6630805684580e7584542e1a");
        account.setAccountNumber("123456789098765");
        account.setAccountHolderName("SAMSON");
        account.setCVV("987");
        account.setBalance(1000.8);

        PassengerDTO passengerDTO = new PassengerDTO();
        passengerDTO.setPassengerTrainId("6630805684580e7584542e1a");
        passengerDTO.setPassengerName("Samson");
        passengerDTO.setAge(31);
        passengerDTO.setDate("14/04/24");
        passengerDTO.setFrom("Delhi");
        passengerDTO.setTo("Chennai");
        passengerDTO.setBerthPreference("U");
        passengerDTO.setEmailId("abdulaziz2002ece@gmail.com");
        passengerDTO.setAccount(account);

        when(northAccountService.findAccountById("6630805684580e7584542e1a")).thenReturn(Optional.of(account));
        when(northIndianTrainService.findTrainById("6630805684580e7584542e1a")).thenReturn(Optional.of(train));

        Passenger bookedPassenger = bookingAndCancellingService.bookPassenger(passengerDTO);

        assertNotNull(bookedPassenger);
        assertEquals("WL",bookedPassenger.getAllowedPreference());
        verify(northPassengerService,times(2)).savePassenger(any());
        verify(northPaymentService,times(1)).savePayment(any());
        verify(northIndianTrainService,times(1)).updateTrain(any());
        verify(southPassengerService,times(1)).savePassenger(any());
        verify(emailService,times(1)).sentEmail(anyString(),anyString(),anyString());
    }


    @Test
    void bookTicketForPassenger_Failure_no_tickets_available(){
        Train train = new Train();
        train.setId("6630805684580e7584542e1a");
        train.setTrainName("Delhi Express");
        train.setDate("14/04/24");
        train.setFrom("Delhi");
        train.setTo("Chennai");
        train.setUpperBerthCount(0);
        train.setLowerBerthCount(0);
        train.setMiddleBerthCount(0);
        train.setRacCount(0);
        train.setWaitingCount(0);
        train.setHomeRegion("NR");
        train.setFromRegion("NR");
        train.setToRegion("SR");
        train.setTrainFee(456.99);


        Account account = new Account();
        account.setId("6630805684580e7584542e1a");
        account.setAccountNumber("123456789098765");
        account.setAccountHolderName("SAMSON");
        account.setCVV("987");
        account.setBalance(1000.8);

        PassengerDTO passengerDTO = new PassengerDTO();
        passengerDTO.setPassengerTrainId("6630805684580e7584542e1a");
        passengerDTO.setPassengerName("Samson");
        passengerDTO.setAge(31);
        passengerDTO.setDate("14/04/24");
        passengerDTO.setFrom("Delhi");
        passengerDTO.setTo("Chennai");
        passengerDTO.setBerthPreference("U");
        passengerDTO.setEmailId("abdulaziz2002ece@gmail.com");
        passengerDTO.setAccount(account);

        when(northAccountService.findAccountById("6630805684580e7584542e1a")).thenReturn(Optional.of(account));
        when(northIndianTrainService.findTrainById("6630805684580e7584542e1a")).thenReturn(Optional.of(train));


        NoTicketsAvailableException noTicketsAvailableException = assertThrows(NoTicketsAvailableException.class, () -> {
            bookingAndCancellingService.bookPassenger(passengerDTO);
        });

        assertEquals("No tickets available",noTicketsAvailableException.getMessage());
    }


    @Test
    void bookTicketForPassenger_Failure_TrainId_is_null(){
        PassengerDTO passengerDTO = new PassengerDTO();
        passengerDTO.setPassengerTrainId(null);

        InvalidDetailsException invalidDetailsException = assertThrows(InvalidDetailsException.class, () -> {
            bookingAndCancellingService.bookPassenger(passengerDTO);
        });

        assertEquals("Train ID is null",invalidDetailsException.getMessage());
        verifyNoInteractions(northIndianTrainService);
        verifyNoInteractions(northPassengerService);
        verifyNoInteractions(northPaymentService);
        verifyNoInteractions(southPassengerService);
        verifyNoInteractions(emailService);
    }

    @Test
    void bookTicketForPassenger_Failure_TrainId_is_empty(){
        PassengerDTO passengerDTO = new PassengerDTO();
        passengerDTO.setPassengerTrainId("");

        InvalidDetailsException invalidDetailsException = assertThrows(InvalidDetailsException.class, () -> {
            bookingAndCancellingService.bookPassenger(passengerDTO);
        });

        assertEquals("Train ID is empty",invalidDetailsException.getMessage());
        verifyNoInteractions(northIndianTrainService);
        verifyNoInteractions(northPassengerService);
        verifyNoInteractions(northPaymentService);
        verifyNoInteractions(southPassengerService);
        verifyNoInteractions(emailService);
    }

    @Test
    void bookTicketForPassenger_Failure_bank_account_number_is_invalid(){
        Account account = new Account();
        account.setId("6630805684580e7584542e1a");
        account.setAccountNumber("123");
        account.setAccountHolderName("SAMSON");
        account.setCVV("987");
        account.setBalance(1000.8);

        PassengerDTO passengerDTO = new PassengerDTO();
        passengerDTO.setPassengerTrainId("6630805684580e7584542e1a");
        passengerDTO.setPassengerName("Samson");
        passengerDTO.setAge(31);
        passengerDTO.setDate("14/04/24");
        passengerDTO.setFrom("Delhi");
        passengerDTO.setTo("Chennai");
        passengerDTO.setBerthPreference("M");
        passengerDTO.setEmailId("abdulaziz2002ece@gmail.com");
        passengerDTO.setAccount(account);


        BankDetailsException exception = assertThrows(BankDetailsException.class ,()-> bookingAndCancellingService.bookPassenger(passengerDTO));

        assertEquals("Account number is Invalid,Check the bank details",exception.getMessage());
        verifyNoInteractions(northIndianTrainService);
        verifyNoInteractions(northPassengerService);
        verifyNoInteractions(northPaymentService);
        verifyNoInteractions(southPassengerService);
        verifyNoInteractions(emailService);
        verifyNoInteractions(northAccountService);
    }

    @Test
    void bookTicketForPassenger_Failure_From_Details_is_null(){
        PassengerDTO passengerDTO = new PassengerDTO();
        passengerDTO.setPassengerTrainId("66307e8c84580e7584542e19");
        passengerDTO.setBerthPreference("L");
        passengerDTO.setFrom(null);
        InvalidDetailsException invalidDetailsException = assertThrows(InvalidDetailsException.class, () -> {
            bookingAndCancellingService.bookPassenger(passengerDTO);
        });

        assertEquals("from details is null",invalidDetailsException.getMessage());
        verifyNoInteractions(northIndianTrainService);
        verifyNoInteractions(northPassengerService);
        verifyNoInteractions(northPaymentService);
        verifyNoInteractions(southPassengerService);
        verifyNoInteractions(emailService);
    }

    @Test
    void bookTicketForPassenger_Failure_From_Details_is_empty(){
        PassengerDTO passengerDTO = new PassengerDTO();
        passengerDTO.setPassengerTrainId("66307e8c84580e7584542e19");
        passengerDTO.setBerthPreference("M");
        passengerDTO.setFrom("");
        InvalidDetailsException invalidDetailsException = assertThrows(InvalidDetailsException.class, () -> {
            bookingAndCancellingService.bookPassenger(passengerDTO);
        });

        assertEquals("from details is empty",invalidDetailsException.getMessage());
        verifyNoInteractions(northIndianTrainService);
        verifyNoInteractions(northPassengerService);
        verifyNoInteractions(northPaymentService);
        verifyNoInteractions(southPassengerService);
        verifyNoInteractions(emailService);
    }

    @Test
    void bookTicketForPassenger_Failure_To_Details_is_empty(){
        PassengerDTO passengerDTO = new PassengerDTO();
        passengerDTO.setPassengerTrainId("66307e8c84580e7584542e19");
        passengerDTO.setBerthPreference("L");
        passengerDTO.setFrom("Delhi");
        passengerDTO.setTo("");
        InvalidDetailsException invalidDetailsException = assertThrows(InvalidDetailsException.class, () -> {
            bookingAndCancellingService.bookPassenger(passengerDTO);
        });

        assertEquals("to details is empty",invalidDetailsException.getMessage());
        verifyNoInteractions(northIndianTrainService);
        verifyNoInteractions(northPassengerService);
        verifyNoInteractions(northPaymentService);
        verifyNoInteractions(southPassengerService);
        verifyNoInteractions(emailService);
    }

    @Test
    void bookTicketForPassenger_Failure_To_Details_is_null(){
        PassengerDTO passengerDTO = new PassengerDTO();
        passengerDTO.setPassengerTrainId("66307e8c84580e7584542e19");
        passengerDTO.setBerthPreference("U");
        passengerDTO.setFrom("Delhi");
        passengerDTO.setTo(null);
        InvalidDetailsException invalidDetailsException = assertThrows(InvalidDetailsException.class, () -> {
            bookingAndCancellingService.bookPassenger(passengerDTO);
        });

        assertEquals("to details is null",invalidDetailsException.getMessage());
        verifyNoInteractions(northIndianTrainService);
        verifyNoInteractions(northPassengerService);
        verifyNoInteractions(northPaymentService);
        verifyNoInteractions(southPassengerService);
        verifyNoInteractions(emailService);
    }

}