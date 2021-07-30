package ru.otus.equation.services;

import ru.otus.container.annotation.Autowired;
import ru.otus.container.annotation.Component;
import ru.otus.equation.model.Player;

@Component(name = "playerService")
public class PlayerServiceImpl implements PlayerService {

    @Autowired
    private IOService ioService;

//    @Autowired
//    public PlayerServiceImpl(IOService ioService) {
//        this.ioService = ioService;
//    }

    @Override
    public Player getPlayer() {
        ioService.out("Представьтесь пожалуйста");
        String playerName = ioService.readLn("Введите имя: ");
        return new Player(playerName);
    }
}
