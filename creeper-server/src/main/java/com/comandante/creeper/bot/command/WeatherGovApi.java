package com.comandante.creeper.bot.command;

import com.google.gson.JsonElement;

public interface WeatherGovApi {
    JsonElement getAlertData(String latitude, String longitude);
}
