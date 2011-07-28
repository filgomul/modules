package org.motechproject.server.messagecampaign.dao;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.io.IOUtils;
import org.motechproject.server.messagecampaign.domain.Campaign;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

public class AllMessageCampaigns {

    public static final String MESSAGECAMPAIGN_DEFINITION_FILE = "messagecampaign.definition.file";

    @Qualifier("messageCampaignProperties")
    private Properties properties;

    @Autowired
    public AllMessageCampaigns(Properties properties) {
        this.properties = properties;
    }

    public Campaign get(String name) {
        List<Campaign> campaigns = getAll();

        for (Campaign campaign : campaigns) {
            if (campaign.getName().equals(name)) return campaign;
        }
        return null;
    }

    private List<Campaign> getAll() {
        String jsonText = getJSON();
        return getFromJSON(jsonText);
    }

    private List<Campaign> getFromJSON(String jsonText) {
        Type campaignListType = new TypeToken<List<Campaign>>() {}.getType();

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Date.class, new DateDeserializer());
        Gson gson = gsonBuilder.create();
        List<Campaign> allCampaigns = gson.fromJson(jsonText, campaignListType);

        if(allCampaigns == null) return new ArrayList<Campaign>();

        return allCampaigns;
    }

    private String getJSON() {
        String jsonText = "";

        InputStream inputStream = ClassLoader.getSystemResourceAsStream(definitionFile());
        if (inputStream == null) return jsonText;

        try {
            jsonText = IOUtils.toString(inputStream);
        } catch (IOException e) {
            LoggerFactory.getLogger(AllMessageCampaigns.class).error("Error reading message campaign definitions - " + e.getMessage());
        }
        return jsonText;
    }

    private String definitionFile() {
        return this.properties.getProperty(MESSAGECAMPAIGN_DEFINITION_FILE);
    }

    private class DateDeserializer implements JsonDeserializer<Date> {
        public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            JsonPrimitive asJsonPrimitive = json.getAsJsonPrimitive();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date date = null;
            try {
                date = format.parse(asJsonPrimitive.getAsString());
            } catch (ParseException e) {
                // TODO
            }
            return date;
        }
    }
}
