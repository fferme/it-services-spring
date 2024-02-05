package com.ferme.itservices.controllers;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@Transactional
@CrossOrigin(origins = "*")
@RequestMapping("/api/clients")
public class ClientController {


}