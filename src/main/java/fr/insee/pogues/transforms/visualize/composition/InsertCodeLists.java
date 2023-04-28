package fr.insee.pogues.transforms.visualize.composition;

import fr.insee.pogues.model.CodeLists;
import fr.insee.pogues.model.Questionnaire;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Set;

/**
 * Implementation of CompositionStep to insert code lists of a referenced questionnaire.
 */
@Slf4j
class InsertCodeLists implements CompositionStep {

    /** Host questionnaire. */
    private Questionnaire questionnaire;
    /** Host questionnaire code list labels. */
    private final Set<String> codeListLabels = new HashSet<>();

    /**
     * Insert code lists of the referenced questionnaire in the referencing questionnaire.
     * If a code list of the referenced questionnaire has the same label as a list in the referencing questionnaire,
     * the code list is not added.
     * @param questionnaire Referencing questionnaire.
     * @param referencedQuestionnaire Referenced questionnaire.
     */
    @Override
    public void apply(Questionnaire questionnaire, Questionnaire referencedQuestionnaire) {
        //
        this.questionnaire = questionnaire;
        //
        CodeLists refCodeLists = referencedQuestionnaire.getCodeLists();
        if (refCodeLists != null) {
            //
            hostCodeLists();
            //
            if (questionnaire.getCodeLists() == null)
                questionnaire.setCodeLists(new CodeLists());
            //
            refCodeLists.getCodeList().forEach(codeList -> {
                if (! codeListLabels.contains(codeList.getLabel()))
                    questionnaire.getCodeLists().getCodeList().add(codeList);
                else
                    log.info("Code list with label '{}' is already in host questionnaire '{}', " +
                            "so it has not been inserted from reference '{}'",
                            codeList.getLabel(), questionnaire.getId(), referencedQuestionnaire.getId());
            });
            log.info("Code lists from '{}' inserted in '{}'", referencedQuestionnaire.getId(), questionnaire.getId());
        } else {
            log.info("No code lists in referenced questionnaire '{}'", referencedQuestionnaire.getId());
        }
    }

    private void hostCodeLists() {
        if (questionnaire.getCodeLists() != null)
            questionnaire.getCodeLists().getCodeList().forEach(codeList -> codeListLabels.add(codeList.getLabel()));
    }

}
