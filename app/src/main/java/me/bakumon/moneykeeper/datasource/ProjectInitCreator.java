package me.bakumon.moneykeeper.datasource;

import android.content.res.Resources;

import java.util.ArrayList;
import java.util.List;

import me.bakumon.moneykeeper.App;
import me.bakumon.moneykeeper.R;
import me.bakumon.moneykeeper.database.entity.Project;
import me.bakumon.moneykeeper.database.entity.Store;

public class ProjectInitCreator {
    public static Project[] createProjectData() {
        List<Project> list = new ArrayList<>();
        Resources res = App.getINSTANCE().getResources();
        Project project;
        project = new Project(res.getString(R.string.text_project_init),String.valueOf(R.drawable.ic_no_project),1);
        list.add(project);
        project = new Project(res.getString(R.string.text_project_reimbursement),String.valueOf(R.drawable.ic_pay_off),2);
        list.add(project);
        project = new Project(res.getString(R.string.text_project_out),String.valueOf(R.drawable.ic_businiess),3);
        list.add(project);
        project = new Project(res.getString(R.string.text_project_celebration),String.valueOf(R.drawable.ic_happy_new_year),4);
        list.add(project);
        project = new Project(res.getString(R.string.text_project_decoration),String.valueOf(R.drawable.ic_decorate),5);
        list.add(project);
        project = new Project(res.getString(R.string.text_shop_other),String.valueOf(R.drawable.ic_others),6);
        list.add(project);
        return list.toArray(new Project[list.size()]);
    }
}