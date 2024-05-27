package com.example.demo.service.booking_and_cancelling;

import com.example.demo.dto.CancelDTO;
import com.example.demo.dto.PassengerDTO;
import com.example.demo.exception.BankDetailsException;
import com.example.demo.exception.InvalidDetailsException;
import com.example.demo.exception.NoTicketsAvailableException;
import com.example.demo.mapper.Mapper;
import com.example.demo.model.Account;
import com.example.demo.model.Passenger;
import com.example.demo.model.Payment;
import com.example.demo.model.Train;
import com.example.demo.service.account_service.*;
import com.example.demo.service.booking_and_cancelling.BookingAndCancellingService;
import com.example.demo.service.email_service.EmailService;
import com.example.demo.service.passImpl.*;
import com.example.demo.service.paymentImpl.*;
import com.example.demo.service.trainImpl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class BookingAndCancellingServiceImpl implements BookingAndCancellingService {



    @Autowired
    private EmailService emailService;
    //Train Service
    @Autowired
    private EastIndianTrainServiceImpl eastIndianTrainService;

    @Autowired
    private SouthIndianTrainServiceImpl southIndianTrainService;

    @Autowired
    private WestIndianTrainServiceImpl westIndianTrainService;

    @Autowired
    private NorthIndianTrainServiceImpl northIndianTrainService;


    //Passenger service

    @Autowired
    private EastPassengerService eastPassengerService;

    @Autowired
    private NorthPassengerService northPassengerService;

    @Autowired
    private SouthPassengerService southPassengerService;

    @Autowired
    private WestPassengerService westPassengerService;


    //Payment service

    @Autowired
    private EastPaymentService eastPaymentService;

    @Autowired
    private NorthPaymentService northPaymentService;

    @Autowired
    private SouthPaymentService southPaymentService;

    @Autowired
    private WestPaymentService westPaymentService;

    @Autowired
    private NorthAccountService northAccountService;

    @Autowired
    private SouthAccountService southAccountService;

    @Autowired
    private EastAccountService eastAccountService;

    @Autowired
    private WestAccountService westAccountService;



    private AccountService getAccountServiceFromRegion(String region){
        if (region.equals("NR")){
            return northAccountService;
        }
        else if (region.equals("SR")){
            return southAccountService;
        }
        else if (region.equals("WR")){
            return westAccountService;
        }
        else if (region.equals("ER")){
            return eastAccountService;
        }
        return null;
    }


    private boolean checkBerthPreferenceIsValid(String preference){
        if (preference.equals("L") || preference.equals("M") || preference.equals("U")){
            return true;
        }
        return false;
    }

    //getting particular train service
    private TrainService getTrainService(String region){
        if (region.equals("NR")){
            return northIndianTrainService;
        }
        else if (region.equals("SR")){
            return southIndianTrainService;
        }
        else if (region.equals("WR")){
            return westIndianTrainService;
        }
        else if (region.equals("ER")){
            return eastIndianTrainService;
        }
        else {
            return null;
        }
    }


    //getting particular passenger service

    private PassengerService getPassengerService(String region){
        if (region.equals("NR")){
            return northPassengerService;
        }
        else if (region.equals("SR")){
            return southPassengerService;
        }
        else if (region.equals("WR")){
            return westPassengerService;
        }
        else if (region.equals("ER")){
            return eastPassengerService;
        }
        else {
            return null;
        }
    }

    //getting a particular payment service

    private PaymentService getPaymentService(String region){
        if (region.equals("NR")){
            return northPaymentService;
        }
        else if (region.equals("SR")){
            return southPaymentService;
        }
        else if (region.equals("WR")){
            return westPaymentService;
        }
        else if (region.equals("ER")){
            return eastPaymentService;
        }
        else {
            return null;
        }
    }




    private void handleBerthCancellation(Train train,Passenger passengerToBeRemoved,int passengerToBeRemovedSeatNumberInTheTrain){


        PassengerService passengerServiceFrom = getPassengerService(getRegionFromCity(passengerToBeRemoved.getFrom()));
        PassengerService passengerServiceTo = getPassengerService(getRegionFromCity(passengerToBeRemoved.getTo()));


        PaymentService paymentServiceFrom = getPaymentService(getRegionFromCity(passengerToBeRemoved.getFrom()));
        PaymentService paymentServiceTo = getPaymentService(getRegionFromCity(passengerToBeRemoved.getTo()));

        TrainService trainService = getTrainService(train.getHomeRegion());





        if (!train.getRacPassengers().isEmpty()){

            Passenger passengerFromRAC = train.getRacPassengers().remove(0);
            passengerFromRAC.setAllowedPreference(passengerToBeRemoved.getAllowedPreference());
            passengerFromRAC.setSeatNumber(passengerToBeRemoved.getSeatNumber());

            assert passengerServiceFrom != null;


            passengerServiceFrom.savePassenger(passengerFromRAC);  //1


            Payment updatedPaymentDetailsOfRACPassenger = passengerFromRAC.getBill();
            updatedPaymentDetailsOfRACPassenger.setBerthPreference(passengerToBeRemoved.getAllowedPreference());
            updatedPaymentDetailsOfRACPassenger.setSeatNumber(passengerToBeRemovedSeatNumberInTheTrain);
            assert paymentServiceFrom != null;

            paymentServiceFrom.savePayment(updatedPaymentDetailsOfRACPassenger); // -> 1

            List<Passenger> berthList = getBerthList(train,passengerToBeRemoved.getAllowedPreference());
            berthList.set(passengerToBeRemovedSeatNumberInTheTrain-1,passengerFromRAC);


            assert passengerServiceTo != null;

            passengerServiceTo.savePassenger(passengerFromRAC);// -> 1

            paymentServiceFrom.delete(passengerToBeRemoved.getBill()); //1

            passengerServiceFrom.delete(passengerToBeRemoved);// 1

            removePassengerFromList(berthList,passengerToBeRemoved);
            removePassengerFromList(train.getBookedPassengers(),passengerToBeRemoved);

            train.getPayments().remove(passengerToBeRemoved.getBill());

            train.setRacCount(train.getRacCount()+1);

            if (!train.getWaitingList().isEmpty()){
                Passenger passengerFromWL = train.getWaitingList().remove(0);
                train.getRacPassengers().add(passengerFromWL);



                passengerFromWL.setAllowedPreference("RAC");
                passengerFromWL.setSeatNumber(train.getRacPassengers().size());

                Payment updatePaymentDetailsForPassengerWL = passengerFromWL.getBill();
                updatePaymentDetailsForPassengerWL.setBerthPreference("RAC");
                updatePaymentDetailsForPassengerWL.setSeatNumber(train.getRacPassengers().size());


                train.setRacCount(train.getRacCount()-1);
                train.setWaitingCount(train.getWaitingCount()+1);

                paymentServiceFrom.savePayment(updatePaymentDetailsForPassengerWL);  // 2
                passengerServiceFrom.savePassenger(passengerFromWL); // 2
                //trainService.saveTrain(train); //1
            }
        }
        else {
            List<Passenger> getPassengerBerthList = getBerthList(train,passengerToBeRemoved.getAllowedPreference());
            if (passengerToBeRemoved.getAllowedPreference().equals("L")){
                train.setLowerBerthCount(train.getLowerBerthCount()+1);

            }
            else if (passengerToBeRemoved.getAllowedPreference().equals("M")){
                train.setMiddleBerthCount(train.getMiddleBerthCount()+1);
            }
            else if (passengerToBeRemoved.getAllowedPreference().equals("U")){
                train.setUpperBerthCount(train.getUpperBerthCount()+1);
            }
            paymentServiceFrom.delete(passengerToBeRemoved.getBill());
            train.getPayments().remove(passengerToBeRemoved.getBill());
            removePassengerFromList(train.getBookedPassengers(),passengerToBeRemoved);
            removePassengerFromList(getPassengerBerthList,passengerToBeRemoved);
            assert passengerServiceFrom != null;
            passengerServiceFrom.delete(passengerToBeRemoved);
        }
        assert trainService != null;
        trainService.saveTrain(train); // 1
    }


    private String checkPreferenceAvailable(String berthPreference, Train train){
        if (berthPreference.equals("L") && train.getLowerBerthCount()>0 ){
            return "LBA";
        }
        else if (berthPreference.equals("M") && train.getMiddleBerthCount()>0 ){
            return "MBA";
        }
        else if (berthPreference.equals("U") && train.getUpperBerthCount()>0){
            return "UBA";
        }
        else {
            return "NA";
        }
    }

    private int getParticularBerthCount(String berthPreference, Train train){
        int output=0;
        if (berthPreference.equals("L")){
            output = train.getLowerBerthCount();
        }
        else if (berthPreference.equals("M")){
            output= train.getMiddleBerthCount();
        }
        else if (berthPreference.equals("U")){
            output= train.getUpperBerthCount();
        }
        return output;
    }

    private String checkFirstPassengersPreference(String berthPreference,Train train){
        if (getParticularBerthCount(berthPreference,train) > 0 && berthPreference.equals("L")) {
            return "L";
        }
        else if (getParticularBerthCount(berthPreference,train) > 0 && berthPreference.equals("M")){
            return "M";
        }
        else if (getParticularBerthCount(berthPreference,train) > 0 && berthPreference.equals("U")){
            return "U";
        }
        return "INV";
    }


    private boolean isValidBerthPreference(String berthPreference){
        return berthPreference!=null && (berthPreference.equals("L") || berthPreference.equals("U")|| berthPreference.equals("M"));
    }

    private String getRegionFromCity(String city){
        List<String> southCities = List.of("Chennai",
                "Thiruvananthapuram",
                "Bangalore",
                "Hyderabad",
                "Coimbatore",
                "Kochin",
                "Madurai",
                "Mysore",
                "Thanjavur",
                "Pondicherry");
        List<String> northCities = List.of("Delhi",
                "Jaipur",
                "Lucknow",
                "Srinagar",
                "Chandigarh",
                "Jammu",
                "Dehradun");
        List<String> eastCities = List.of("Bhubaneswar",
                "Cuttack",
                "Kolkata");
        List<String> westCities = List.of("Aurangabad",
                "Mumbai",
                "Mahabaleshwar",
                "Nagpur",
                "Pune",
                "Shirdi");

        if (northCities.contains(city)){
            return "NR";
        }
        else if (southCities.contains(city)){
            return "SR";
        }
        else if (westCities.contains(city)){
            return "WR";
        }
        else if (eastCities.contains(city)){
            return "ER";
        }
        else {
            return "INV";
        }
    }



    @Override
    public Passenger bookPassenger(PassengerDTO passengerDTO) {

        if ( passengerDTO.getPassengerTrainId() == null){
            throw new InvalidDetailsException("Train ID is null");
        }
        if (passengerDTO.getPassengerTrainId().isEmpty()){
            throw new InvalidDetailsException("Train ID is empty");
        }
        if (passengerDTO.getBerthPreference() == null ){
            throw new InvalidDetailsException("Berth is null");
        }
        if (passengerDTO.getBerthPreference().isEmpty()){
            throw new InvalidDetailsException("Berth is empty");
        }
        if (!checkBerthPreferenceIsValid(passengerDTO.getBerthPreference())){
            throw new InvalidDetailsException("Berth preference is invalid");
        }
        if ( passengerDTO.getFrom() == null){
            throw new InvalidDetailsException("from details is null");
        }
        if (passengerDTO.getFrom().isEmpty()){
            throw new InvalidDetailsException("from details is empty");
        }
        if (passengerDTO.getTo() == null ){
            throw new InvalidDetailsException("to details is null");
        }
        if (passengerDTO.getTo().isEmpty()){
            throw new InvalidDetailsException("to details is empty");
        }

        if (!isValidAccountNumber(passengerDTO.getAccount().getAccountNumber())){
            throw new BankDetailsException("Account number is Invalid,Check the bank details");
        }


        String fromRegion = getRegionFromCity(passengerDTO.getFrom());
        String toRegion = getRegionFromCity(passengerDTO.getTo());

        String trainId = passengerDTO.getPassengerTrainId();

        Optional<Train> optionalTrain = Objects.requireNonNull(getTrainService(fromRegion)).findTrainById(trainId);

        Passenger updatedPassenger= null;

            if (optionalTrain.isEmpty()){
                throw new RuntimeException("Train not found with this train Id : "+ passengerDTO.getPassengerTrainId());
            }

            if (optionalTrain.get().getLowerBerthCount() == 0 && optionalTrain.get().getMiddleBerthCount() == 0 && optionalTrain.get().getUpperBerthCount() == 0 && optionalTrain.get().getRacCount() ==0 && optionalTrain.get().getWaitingCount() == 0){
                throw new NoTicketsAvailableException("No tickets available");
            }

            String berthPreference = passengerDTO.getBerthPreference();
            Train train = optionalTrain.get();

            if (isValidBerthPreference(berthPreference)){
                //passenger's first preference checking
                Passenger passenger = Mapper.convertToPassenger(passengerDTO);
                if (passenger.getAccount().getBalance()>=train.getTrainFee()){
                    passenger.getAccount().setBalance(passenger.getAccount().getBalance() - train.getTrainFee());
                    AccountService getAccountService = getAccountServiceFromRegion(fromRegion);
                    getAccountService.addAccount(passenger.getAccount());
                    if (checkFirstPassengersPreference(berthPreference,train).equals("L")){
                        train.setLowerBerthCount(train.getLowerBerthCount()-1);
                        updatedPassenger= bookTicket(passenger,
                                train,
                                new Payment(train.getTrainName(),trainId,passenger.getPassengerName(),"",berthPreference,0,train.getTrainFee()),
                                berthPreference);
                    }
                    else if (checkFirstPassengersPreference(berthPreference,train).equals("M")){
                        train.setMiddleBerthCount(train.getMiddleBerthCount()-1);
                        updatedPassenger= bookTicket(passenger,
                                train,
                                new Payment(train.getTrainName(),trainId,passenger.getPassengerName(),"",berthPreference,0,train.getTrainFee()),
                                berthPreference);
                    }
                    else if (checkFirstPassengersPreference(berthPreference,train).equals("U")){
                        train.setUpperBerthCount(train.getUpperBerthCount()-1);
                        updatedPassenger= bookTicket(passenger,
                                train,
                                new Payment(train.getTrainName(),trainId,passenger.getPassengerName(),"",berthPreference,0,train.getTrainFee()),
                                berthPreference);
                    }
                    else {
                        if (checkPreferenceAvailable("L",train).equals("LBA")){
                            train.setLowerBerthCount(train.getLowerBerthCount()-1);
                            updatedPassenger= bookTicket(passenger,
                                    train,
                                    new Payment(train.getTrainName(),trainId,passenger.getPassengerName(),"","L",0,train.getTrainFee()),
                                    "L");
                        }
                        else if (checkPreferenceAvailable("M",train).equals("MBA")){
                            train.setMiddleBerthCount(train.getMiddleBerthCount()-1);
                            updatedPassenger= bookTicket(passenger,
                                    train,
                                    new Payment(train.getTrainName(),trainId,passenger.getPassengerName(),"","M",0,train.getTrainFee()),
                                    "M");
                        }
                        else if (checkPreferenceAvailable("U",train).equals("UBA")){
                            train.setUpperBerthCount(train.getUpperBerthCount()-1);
                            updatedPassenger= bookTicket(passenger,
                                    train,
                                    new Payment(train.getTrainName(),trainId,passenger.getPassengerName(),"","U",0,train.getTrainFee()),
                                    "U");
                        }
                        else {
                            if (train.getRacCount() > 0 ){
                                train.setRacCount(train.getRacCount()-1);
                                updatedPassenger= bookTicket(passenger,
                                        train,
                                        new Payment(train.getTrainName(),trainId,passenger.getPassengerName(),"","RAC",0,train.getTrainFee()),
                                        "RAC");
                            }
                            else if (train.getWaitingCount() > 0){
                                train.setWaitingCount(train.getWaitingCount()-1);
                                updatedPassenger= bookTicket(passenger,
                                        train,
                                        new Payment(train.getTrainName(),trainId,passenger.getPassengerName(),"","WL",0,train.getTrainFee()),
                                        "WL");
                            }
                        }
                    }
                }
                else {
                    throw new BankDetailsException("Insufficient Balance in your account");
                }


                if (!fromRegion.equals(toRegion)){
                    PassengerService toPassengerService = getPassengerService(toRegion);
                    assert toPassengerService != null;
                    toPassengerService.savePassenger(updatedPassenger);
                }

            }
            else {
                throw new RuntimeException("Invalid passenger preference");
            }


            String to = passengerDTO.getEmailId();
            String subject = "Ticket Booked Successfully";
            assert updatedPassenger != null;
            String body = "Dear ,"+passengerDTO.getPassengerName()+ "\n"+"Thank you for booking your with us."+"The journey details is attached with this email\n\n\n"+
                "Ticket Details "+"\n\n\n"
                +"Passenger Id       : "+updatedPassenger.getId()+"\n"
                +"Passenger name     : "+ updatedPassenger.getPassengerName()+"\n"
                +"Passenger train name : "+ train.getTrainName()+"\n"
                +"From : "+ updatedPassenger.getFrom()+"\n"
                +"To : "+ updatedPassenger.getTo()+"\n"
                +"Boarding Time : "+ updatedPassenger.getBoardingTime()+"\n"
                +"Dropping Time : "+ updatedPassenger.getDroppingTime()+"\n\n\n\n"
                +"Thanks & Regards ,"+"\n\n"
                +"Board of Indian Railways";
            sentEmailToThePassenger(to,subject,body);
        return updatedPassenger;
    }


    private void removePassengerFromList(List<Passenger> passengerList, Passenger passengerToBeRemoved){
        passengerList.removeIf(passenger -> passenger.getId().equals(passengerToBeRemoved.getId()));
    }


    boolean isValidAccountNumber(String passengerAccountNumber) {
        String accountNumberRegex="\\d{11,16}";
        return Pattern.matches(accountNumberRegex, passengerAccountNumber);
    }

    private void sentEmailToThePassenger(String to,String subject,String body){
        emailService.sentEmail(to,subject,body);
    }

    @Override
    public List<Train> viewAllTrains(String region) {
        List<Train> regionWiseTrains=null;

        TrainService getTrainsFromRegion = getTrainService(region);
        assert getTrainsFromRegion != null;
        regionWiseTrains = getTrainsFromRegion.getAllTrains();

        return regionWiseTrains;
    }

    @Override
    public String cancelBookingForPassenger(CancelDTO cancelDTO) {
        if (cancelDTO.getTrainId() == null ){
            throw new InvalidDetailsException("Train ID is null");
        }
        if (cancelDTO.getTrainId().isEmpty()){
            throw new InvalidDetailsException("Train ID is empty");
        }
        if (cancelDTO.getPassId() == null ){
            throw new InvalidDetailsException("Pass ID is null");
        }
        if (cancelDTO.getPassId().isEmpty()){
            throw new InvalidDetailsException("Pass ID is empty");
        }
        if (cancelDTO.getBerth() == null ){
            throw new InvalidDetailsException("Berth is null");
        }
        if (cancelDTO.getBerth().isEmpty()){
            throw new InvalidDetailsException("Berth is empty");
        }
        if (!checkBerthPreferenceIsValid(cancelDTO.getBerth())){
            throw new InvalidDetailsException("Berth preference is invalid");
        }
        if (cancelDTO.getFrom() == null){
            throw new InvalidDetailsException("from details is null");
        }
        if (cancelDTO.getFrom().isEmpty()){
            throw new InvalidDetailsException("from details is empty");
        }
        if (cancelDTO.getTo() == null){
            throw new InvalidDetailsException("to details is null");
        }
        if (cancelDTO.getTo().isEmpty()){
            throw new InvalidDetailsException("to details is empty");
        }
        String from = getRegionFromCity(cancelDTO.getFrom());
        String to = getRegionFromCity(cancelDTO.getTo());

        String trainId = cancelDTO.getTrainId();
        String passengerId = cancelDTO.getPassId();


        Optional<Train> optionalTrain = Objects.requireNonNull(getTrainService(from)).findTrainById(trainId);
        Optional<Passenger> optionalPassengerToBeRemoved = Objects.requireNonNull(getPassengerService(from)).findPassengerById(passengerId);


        if (optionalTrain.isPresent() && optionalPassengerToBeRemoved.isPresent()){
            Train train = optionalTrain.get();
            Passenger passengerToBeRemoved = optionalPassengerToBeRemoved.get();
            String passengerToBeRemoveName = passengerToBeRemoved.getPassengerName();

            passengerToBeRemoved.getAccount().setBalance(passengerToBeRemoved.getAccount().getBalance() + train.getTrainFee());

            AccountService getAccountService = getAccountServiceFromRegion(from);
            getAccountService.saveAccount(passengerToBeRemoved.getAccount());

            //String cancelledMessage = "Passenger : "+ passengerToBeRemoved.getPassengerName()  + "\n" + "Passenger Id : "+ passengerToBeRemoved.getId() + "\n"+ "Train Name : "+ passengerToBeRemoved.getPassengerTrainName()+"\n"+"Refund Amount : "+ passengerToBeRemoved.getBill().getAmount()+"\n"+"Booking cancelled successfully";

            String cancelledMessage ="Ticket Cancelled Successfully";
            String allowedPreference = passengerToBeRemoved.getAllowedPreference();
            int passengerToBeRemovedInTheTrainSeatNumber = passengerToBeRemoved.getSeatNumber();

            if (allowedPreference.equals("L")|| allowedPreference.equals("M")|| allowedPreference.equals("U")){

                PassengerService toPassengerService = getPassengerService(to);
                assert toPassengerService != null;
                toPassengerService.delete(passengerToBeRemoved); // ->1
                handleBerthCancellation(train,passengerToBeRemoved,passengerToBeRemovedInTheTrainSeatNumber);
                String toRecipient = cancelDTO.getEmailId();
                String subject = "Ticket Cancelled Successfully";
                String body = "Dear ,"+passengerToBeRemoveName + "\n\n\n"+"Your refund amount of Rs : "+train.getTrainFee()+" has been successfully credited to your account";
                sentEmailToThePassenger(toRecipient,subject,body);
                return cancelledMessage;
            }
        }
        return "Something went wrong";
    }

    @Override
    public Optional<Passenger> findPassengerById(String passengerID) {
        return Optional.empty();
    }

    private void setPassengerData(Passenger passenger, Train train, String berthPreference){
        passenger.setPassengerTrainName(train.getTrainName());
        passenger.setAllowedPreference(berthPreference);
        passenger.setBoardingTime(train.getBoardingTime());
        passenger.setDroppingTime(train.getDroppingTime());
        passenger.setDate(train.getDate());
        passenger.setFrom(train.getFrom());
        passenger.setTo(train.getTo());
    }

    private void setPaymentDataForPassenger(Passenger passenger, Train train, Payment payment, String bethPreference, String region){

        PassengerService passengerService = getPassengerService(region);
        assert passengerService != null;
        passengerService.savePassenger(passenger); //1

        payment.setTrainId(train.getId());
        payment.setPassengerId(passenger.getId());
        payment.setTrainName(train.getTrainName());
        payment.setPassengerName(passenger.getPassengerName());
        payment.setBerthPreference(bethPreference);
        payment.setAmount(train.getTrainFee());
    }


    private Passenger bookTicket(Passenger passenger, Train train, Payment payment, String berthPreference) {
        setPassengerData(passenger, train,berthPreference);
        setPaymentDataForPassenger(passenger, train, payment,berthPreference,getRegionFromCity(passenger.getFrom()));
        assignSeatAndSavePayment(passenger, train, payment,berthPreference);
        updateTrainDetails(train, passenger, payment);


        PaymentService paymentService = getPaymentService(getRegionFromCity(passenger.getFrom()));
        PassengerService passengerService = getPassengerService(getRegionFromCity(passenger.getFrom()));
        TrainService trainService = getTrainService(getRegionFromCity(passenger.getFrom()));


        assert paymentService != null;
        paymentService.savePayment(payment); //1
        assert passengerService != null;
        passengerService.savePassenger(passenger); //2
        assert trainService != null;
        trainService.updateTrain(train); //1

        return passenger;
    }

    private void updateTrainDetails(Train train, Passenger passenger, Payment payment){
        train.getBookedPassengers().add(passenger);
        train.getPayments().add(payment);
    }

    private void assignSeatAndSavePayment(Passenger passenger, Train train, Payment payment, String berthPreference){
        int seatNumber = assignSeatNumber(train, passenger,berthPreference);
        payment.setSeatNumber(seatNumber);
        passenger.setSeatNumber(seatNumber);
        passenger.setBill(payment);
    }

    private int assignSeatNumber(Train train, Passenger passenger, String berthPreference){
        List<Passenger> berthList_L_M_U = getBerthList(train,berthPreference);
        berthList_L_M_U.add(passenger);
        return berthList_L_M_U.indexOf(passenger)+1;
    }

    private List<Passenger> getBerthList(Train train, String berthPreference){
        switch (berthPreference){
            case "L":
                return train.getLowerBerth();
            case "M":
                return train.getMiddleBerth();
            case "U":
                return train.getUpperBerth();
            case "RAC":
                return train.getRacPassengers();
            case "WL":
                return train.getWaitingList();
            default:
                return new ArrayList<>();
        }
    }
}