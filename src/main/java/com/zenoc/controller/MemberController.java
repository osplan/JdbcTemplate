package com.zenoc.controller;

import com.zenoc.core.model.JsonResult;
import com.zenoc.core.model.PageParam;
import com.zenoc.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Ty on 2017/7/21.
 */
@Controller
@RequestMapping("/member")
public class MemberController {
    @Autowired
    MemberService memberService;

    @ResponseBody
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String getMembers(){
        JsonResult jr = new JsonResult();
        jr.attr("members",memberService.get());
        return jr.toJson();
    }

    @ResponseBody
    @RequestMapping(value = "/{memberId}",method = RequestMethod.GET)
    public String getMember(@PathVariable("memberId") Long memberId){
        JsonResult jr = new JsonResult();
        jr.attr("member",memberService.get(memberId));
        return jr.toJson();
    }

    @ResponseBody
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String saveMember(String name, @RequestParam(required = false, defaultValue = "0") int age){
        JsonResult jr = new JsonResult();
        jr.attr("member",memberService.save(name,age));
        return jr.toJson();
    }

    @ResponseBody
    @RequestMapping(value = "/modify", method = RequestMethod.POST)
    public String modify(Long memberId, @RequestParam(required = false, defaultValue = "0") int age){
        JsonResult jr = new JsonResult();
        jr.attr("member",memberService.modify(memberId,age));
        return jr.toJson();
    }

    @ResponseBody
    @RequestMapping(value = "/balance/{memberId}", method = RequestMethod.GET)
    public String getBalance(@PathVariable("memberId") Long memberId){
        JsonResult jr = new JsonResult();
        jr.attr("member",memberService.getBalance(memberId));
        return jr.toJson();
    }

    @ResponseBody
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String getPagination(String name, PageParam pageParam){
        JsonResult jr = new JsonResult();
        jr.setPageData(memberService.get(name, pageParam.getPage(1),pageParam.getPageSize(10,50)));
        return jr.toJson();
    }
}
