package com.graduationProject._thYear.Shift.services;

import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.ResourceAccessException;

import com.graduationProject._thYear.Shift.dtos.requests.CloseShiftRequest;
import com.graduationProject._thYear.Shift.dtos.requests.StartShiftRequest;
import com.graduationProject._thYear.Shift.dtos.responses.ShiftResponse;
import com.graduationProject._thYear.Shift.models.Shift;
import com.graduationProject._thYear.Shift.repositories.ShiftRepository;
import com.graduationProject._thYear.exceptionHandler.ResourceNotFoundException;

@Service
@Transactional
@RequiredArgsConstructor
public class ShiftService  {
    private final ShiftRepository shiftRepository;
    
    public ShiftResponse startShift(StartShiftRequest request){
        List<Shift> previouseShifts = shiftRepository.findByUserId(request.getUser().getId(), Sort.by(Order.desc("startDate")));
        if (!previouseShifts.isEmpty() && previouseShifts.getFirst().getEndDate() == null){
            throw new ResourceAccessException("Can't start a new shift before closing the previouse one");
        }
        Shift shift = Shift.builder()
            .user(request.getUser())
            .startDate(LocalDateTime.now())
            .startCash(request.getStartCash())
            .build();

        shiftRepository.save(shift);
        return ShiftResponse.fromShiftEntity(shift);
    }

    

     public ShiftResponse checkShift(Integer userId){
        List<Shift> previouseShifts = shiftRepository.findByUserId(userId, Sort.by(Order.desc("startDate")));
        if (previouseShifts.isEmpty()){
            throw new ResourceNotFoundException("Shift with for user with Id " + userId + " Not Found.");
        }
        Shift shift = previouseShifts.getFirst();

        shift.setExpectedEndCash(shiftRepository.getCurrentCash(shift.getId()));

        return ShiftResponse.fromShiftEntity(shift);
    }
   
    public ShiftResponse closeShift(Integer userId, CloseShiftRequest request){
        List<Shift> previouseShifts = shiftRepository.findByUserId(userId, Sort.by(Order.desc("startDate")));
        if (previouseShifts.isEmpty()){
            throw new ResourceNotFoundException("Shift with for user with Id " + userId + " Not Found.");
        }
        Shift shift = previouseShifts.getFirst();

        if (shift.getEndDate() != null){
            throw new ResourceAccessException("Can't close an already closed shift");
        }

        shift.setEndCash(request.getEndCash());
        shift.setEndDate(LocalDateTime.now());
        shift.setExpectedEndCash(shiftRepository.getEndCash(shift.getId()));
        shift.setNotes(request.getNotes());

        shiftRepository.save(shift);
        return ShiftResponse.fromShiftEntity(shift);
    }



    public List<ShiftResponse> listShifts(Integer userId, LocalDateTime startDate, LocalDateTime endDate, Boolean isClosed) {
        List<Shift> shifts = shiftRepository.listShifts(userId, startDate, endDate, isClosed, Sort.by(Order.desc("startDate")));
        return shifts.stream()
            .map(ShiftResponse::fromShiftEntity)
            .collect(Collectors.toList());
    }
   
}
