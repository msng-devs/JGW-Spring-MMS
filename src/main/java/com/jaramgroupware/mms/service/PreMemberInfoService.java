package com.jaramgroupware.mms.service;

import com.jaramgroupware.mms.domain.major.MajorRepository;
import com.jaramgroupware.mms.domain.memberView.MemberView;
import com.jaramgroupware.mms.domain.preMemberInfo.PreMemberInfo;
import com.jaramgroupware.mms.domain.preMemberInfo.PreMemberInfoRepository;
import com.jaramgroupware.mms.domain.rank.RankRepository;
import com.jaramgroupware.mms.domain.registerCode.RegisterCode;
import com.jaramgroupware.mms.domain.registerCode.RegisterCodeRepository;
import com.jaramgroupware.mms.domain.role.RoleRepository;
import com.jaramgroupware.mms.dto.preMemberInfo.PreMemberInfoResponseDto;
import com.jaramgroupware.mms.dto.preMemberInfo.serviceDto.PreMemberInfoAddRequestServiceDto;
import com.jaramgroupware.mms.dto.preMemberInfo.serviceDto.PreMemberInfoUpdateRequestServiceDto;
import com.jaramgroupware.mms.utils.exception.service.ServiceErrorCode;
import com.jaramgroupware.mms.utils.exception.service.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@RequiredArgsConstructor
@Service
public class PreMemberInfoService {

    private PreMemberInfoRepository preMemberInfoRepository;
    private MajorRepository majorRepository;
    private RankRepository rankRepository;
    private RoleRepository roleRepository;
    private MemberService memberService;
    private RegisterCodeRepository registerCodeRepository;

    @Transactional(readOnly = true)
    public PreMemberInfoResponseDto findById(Long id) {
        var targetPreMemberInfo = preMemberInfoRepository.findById(id)
                .orElseThrow(() -> new ServiceException(ServiceErrorCode.NOT_FOUND, "존재하지 않는 PreMemberInfo입니다."));

        return new PreMemberInfoResponseDto(targetPreMemberInfo);
    }

    @Transactional(readOnly = true)
    public List<PreMemberInfoResponseDto> findAll(Specification<PreMemberInfo> specification, Pageable pageable){
        var preMemberInfos = preMemberInfoRepository.findAll(specification,pageable);
        return preMemberInfos.stream().map(PreMemberInfoResponseDto::new).toList();
    }

    @Transactional
    public void deletePreMemberInfo(Long id) {
        var targetPreMemberInfo = preMemberInfoRepository.findById(id)
                .orElseThrow(() -> new ServiceException(ServiceErrorCode.NOT_FOUND, "존재하지 않는 PreMemberInfo입니다."));

        preMemberInfoRepository.delete(targetPreMemberInfo);
    }

    @Transactional
    public PreMemberInfoResponseDto createPreMemberInfo(PreMemberInfoAddRequestServiceDto requestDto) {

        var targetRole = roleRepository.findRoleById(requestDto.getRoleId())
                .orElseThrow(() -> new ServiceException(ServiceErrorCode.NOT_FOUND, "존재하지 않는 Role입니다."));
        var targetRank = rankRepository.findById(requestDto.getRankId())
                .orElseThrow(() -> new ServiceException(ServiceErrorCode.NOT_FOUND, "존재하지 않는 Rank입니다."));
        var targetMajor = majorRepository.findById(requestDto.getMajorId())
                .orElseThrow(() -> new ServiceException(ServiceErrorCode.NOT_FOUND, "존재하지 않는 Major입니다."));

        memberService.isExistsStudentId(requestDto.getStudentId());
        isExistsStudentId(requestDto.getStudentId());

        var preMemberInfo = requestDto.toEntity(targetRole, targetRank, targetMajor);

        var newPreMemberInfo = preMemberInfoRepository.save(preMemberInfo);

        return new PreMemberInfoResponseDto(newPreMemberInfo);

    }

    @Transactional
    public PreMemberInfoResponseDto updatePreMemberInfo(PreMemberInfoUpdateRequestServiceDto requestDto) {

        var targetPreMemberInfo = preMemberInfoRepository.findById(requestDto.getId())
                .orElseThrow(() -> new ServiceException(ServiceErrorCode.NOT_FOUND, "존재하지 않는 PreMemberInfo입니다."));

        var targetRole = roleRepository.findRoleById(requestDto.getRoleId())
                .orElseThrow(() -> new ServiceException(ServiceErrorCode.NOT_FOUND, "존재하지 않는 Role입니다."));
        var targetRank = rankRepository.findById(requestDto.getRankId())
                .orElseThrow(() -> new ServiceException(ServiceErrorCode.NOT_FOUND, "존재하지 않는 Rank입니다."));
        var targetMajor = majorRepository.findById(requestDto.getMajorId())
                .orElseThrow(() -> new ServiceException(ServiceErrorCode.NOT_FOUND, "존재하지 않는 Major입니다."));

        memberService.isExistsStudentId(requestDto.getStudentId());
        isExistsStudentId(requestDto.getStudentId());

        targetPreMemberInfo.update(requestDto.toEntity(targetRole, targetRank, targetMajor));

        var newPreMemberInfo = preMemberInfoRepository.save(targetPreMemberInfo);
        return new PreMemberInfoResponseDto(newPreMemberInfo);
    }


    @Transactional(readOnly = true)
    public void isExistsStudentId(String studentId) {
        if (preMemberInfoRepository.existsByStudentId(studentId)) {
            throw new ServiceException(ServiceErrorCode.ALREADY_EXISTS, "이미 존재하는 학번입니다.");
        }
    }
}
