package com.ali.mirsalari.wrench.config;

import com.ali.mirsalari.wrench.entity.Service;
import com.ali.mirsalari.wrench.exception.*;
import com.ali.mirsalari.wrench.service.ServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Component
@RequiredArgsConstructor
public class ServiceConfig {
    private final ServiceService serviceService;
    public void start() {
        addServices();
        addSubServices();
    }

    public void addServices() {
        Service service1 = new Service("دکوراسیون ساختمان");
        Service service2 = new Service("تاسیسات ساختمان");
        Service service3 = new Service("وسایل نقلیه");
        Service service4 = new Service("اسباب کسی و باربری");
        Service service5 = new Service("لوازم خانگی");
        Service service6 = new Service("نظافت و بهداشت");

        try {
            serviceService.save(service1);
            serviceService.save(service2);
            serviceService.save(service3);
            serviceService.save(service4);
            serviceService.save(service5);
            serviceService.save(service6);
        } catch (ServiceExistException e) {
            System.out.println(e.getMessage());
        }

    }

    public void addSubServices() {
        Optional<Service> serviceOptional = serviceService.findByName("لوازم خانگی");
        if (serviceOptional.isPresent()) {
            Service service1 = serviceOptional.get();
            Service subervice1 = new Service("لوازم آشپزخانه", 100_000L, "خدمات مربوط به لوازم آشپزخانه", service1);
            Service subervice2 = new Service("رختشویی", 200_000L, "خدمات مربوط به رختشویی", service1);
            Service subervice3 = new Service("لوازم صوتی و تصویری", 1_000_000L, "خدمات مربوط به لوازم صوتی و تصویری", service1);
            try {
                serviceService.save(subervice1);
                serviceService.save(subervice2);
                serviceService.save(subervice3);
            } catch (ServiceExistException e) {
                System.out.println(e.getMessage());

            }

        }
        Optional<Service> serviceOptional2 = serviceService.findByName("نظافت و بهداشت");

        if (serviceOptional2.isPresent()) {
            Service service2 = serviceOptional2.get();
            Service subervice4 = new Service("نظافت", 100_000L, "خدمات مربوط به نظافت", service2);
            Service subervice5 = new Service("خشکشویی و اتوشویی", 200_000L, "خدمات مربوط به خشکشویی و اتوشویی", service2);
            Service subervice6 = new Service("قالیشویی و مبل شویی", 1_000_000L, "خدمات مربوط به قالیشویی و مبل شویی", service2);
            Service subervice7 = new Service("سمپاشی منازل", 10_000_000L, "خدمات مربوط به سمپاشی منازل", service2);
            try {
                serviceService.save(subervice4);
                serviceService.save(subervice5);
                serviceService.save(subervice6);
                serviceService.save(subervice7);
            } catch (ServiceExistException e) {
                System.out.println(e.getMessage());
            }

        }
    }

}
