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

    

     public ShiftResponse checkShift(Integer shiftId){
        Shift shift = shiftRepository.findById(shiftId)
            .orElseThrow(() -> new ResourceNotFoundException("Shift with Id " + shiftId + " Not Found."));

        shift.setExpectedEndCash(shiftRepository.getCurrentCash(shiftId).add(shift.getStartCash()));

        return ShiftResponse.fromShiftEntity(shift);
    }
   
    public ShiftResponse closeShift(Integer shiftId, CloseShiftRequest request){
        Shift shift = shiftRepository.findById(shiftId)
            .orElseThrow(() -> new ResourceNotFoundException("Shift with Id " + shiftId + " Not Found."));

        if (shift.getEndDate() != null){
            throw new ResourceAccessException("Can't close an already closed shift");
        }

        shift.setEndCash(request.getEndCash());
        shift.setEndDate(LocalDateTime.now());
        shift.setExpectedEndCash(shiftRepository.getEndCash(shiftId).add(shift.getStartCash()));
        shift.setNotes(request.getNotes());

        shiftRepository.save(shift);
        return ShiftResponse.fromShiftEntity(shift);
    }



    public List<ShiftResponse> listShifts(Integer userId, LocalDateTime startDate, LocalDateTime endDate, Boolean isClosed) {
        List<Shift> shifts = shiftRepository.listShifts(userId, startDate, endDate, isClosed, Sort.by(Order.desc("startDate")));
        System.out.println(isClosed);
        return shifts.stream()
            .map(ShiftResponse::fromShiftEntity)
            .collect(Collectors.toList());
    }
   
}
