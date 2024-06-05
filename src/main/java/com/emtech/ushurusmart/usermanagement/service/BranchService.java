package com.emtech.ushurusmart.usermanagement.service;

import com.emtech.ushurusmart.usermanagement.Dtos.entity.BranchDto;
import com.emtech.ushurusmart.usermanagement.model.Branch;
import com.emtech.ushurusmart.usermanagement.repository.BranchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BranchService {

    @Autowired



    private BranchRepository branchRepository;

    public List<BranchDto> getAllBranches() {
        return branchRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Optional<BranchDto> getBranchById(Long id) {
        return branchRepository.findById(id).map(this::convertToDto);
    }

    public BranchDto createBranch(BranchDto branchDto) {
        Branch branch = convertToEntity(branchDto);
        Branch savedBranch = branchRepository.save(branch);
        return convertToDto(savedBranch);
    }

    public BranchDto updateBranch(Long id, BranchDto branchDto) {
        Branch branch = branchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Branch not found with id " + id));
        branch.setName(branchDto.getName());
        branch.setLocation(branchDto.getLocation());
        branch.setSupervisor(branchDto.getSupervisor());
        Branch updatedBranch = branchRepository.save(branch);
        return convertToDto(updatedBranch);
    }

    public void deleteBranch(Long id) {
        Branch branch = branchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Branch not found with id " + id));
        branchRepository.delete(branch);
    }

    private BranchDto convertToDto(Branch branch) {
        return new BranchDto(branch.getName(), branch.getLocation(), branch.getSupervisor());
    }

    private Branch convertToEntity(BranchDto branchDto) {
        Branch branch = new Branch();
        branch.setName(branchDto.getName());
        branch.setLocation(branchDto.getLocation());
        branch.setSupervisor(branchDto.getSupervisor());
        return branch;
    }
}
