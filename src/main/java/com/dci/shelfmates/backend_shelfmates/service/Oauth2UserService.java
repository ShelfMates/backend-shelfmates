package com.dci.shelfmates.backend_shelfmates.service;

import com.dci.shelfmates.backend_shelfmates.model.User;
import com.dci.shelfmates.backend_shelfmates.model.UserProvider;
import com.dci.shelfmates.backend_shelfmates.repository.UserProviderRepository;
import com.dci.shelfmates.backend_shelfmates.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class Oauth2UserService extends DefaultOAuth2UserService {

    // injects the relevant repos
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserProviderRepository userProviderRepository;

    // if needed we can get tokens here to access the api on the users behalf
    // for example if we need to get access to a google calender - otherwise everything else is handled frontend wise

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) {
        // get the user
        OAuth2User oAuth2User = super.loadUser(request);

        // not sure if this is right and returns actually a string with the provider name - we'll see
        String provider = request.getClientRegistration().getRegistrationId(); // get the provider name
        String providerUserId = oAuth2User.getAttribute("sub"); // get the user id from provider
        String email = oAuth2User.getAttribute("email"); // get the mail address

    // lookup if user already exists in db
    // if the optional is empty the lambda function will execute
        UserProvider userProvider = userProviderRepository.findByProviderAndProviderUserId(provider, providerUserId)
                .orElseGet(() -> {
                    User user = userRepository.findByEmail(email).orElseGet(() -> {
                        User newUser = new User();
                        newUser.setEmail(email);
                        newUser.setDisplayName(oAuth2User.getAttribute("name"));
                        return userRepository.save(newUser);
                    });

                    UserProvider newProvider = new UserProvider();
                    newProvider.setUser(user);
                    newProvider.setProvider(provider);
                    newProvider.setProviderUserId(providerUserId);
                    newProvider.setProviderEmail(email);
                    return userProviderRepository.save(newProvider);

                });




        return oAuth2User;
    }


}
