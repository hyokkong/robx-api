package com.rbox.admin.policy;

public record SizePolicy(String spcCd, String szCd, Integer ageMin, Integer ageMax,
                         Double wtMin, Double wtMax, String adjTp) {}
