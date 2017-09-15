package fr.insee.pogues.metadata.client;

import fr.insee.pogues.metadata.model.ColecticaItem;
import fr.insee.pogues.metadata.model.ColecticaItemRefList;

import java.util.List;

public interface MetadataClient {

    ColecticaItem getItem(String id) throws Exception;
    List<ColecticaItem> getItems(ColecticaItemRefList query) throws Exception;
    ColecticaItemRefList getChildrenRef(String id) throws Exception;
}
