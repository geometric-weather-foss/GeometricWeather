package com.mbestavros.geometricweather.settings.model;

import java.util.ArrayList;
import java.util.List;

import com.mbestavros.geometricweather.R;

/**
 * About app translator.
 * */

public class AboutAppTranslator {

    public String name;
    public String email;
    public int flagResId;

    private AboutAppTranslator(String name, String email, int flagResId) {
        this.name = name;
        this.email = email;
        this.flagResId = flagResId;
    }

    public static List<AboutAppTranslator> buildTranslatorList() {
        List<AboutAppTranslator> list = new ArrayList<>(21);
        list.add(new AboutAppTranslator(
                "Mehmet Saygin Yilmaz",
                "memcos@gmail.com",
                R.drawable.flag_tr
        ));
        list.add(new AboutAppTranslator(
                "Ali D.",
                "siyaha@gmail.com",
                R.drawable.flag_tr
        ));
        list.add(new AboutAppTranslator(
                "benjamin Tourrel",
                "polo_naref@hotmail.fr",
                R.drawable.flag_fr
        ));
        list.add(new AboutAppTranslator(
                "Roman Adadurov",
                "orelars53@gmail.com",
                R.drawable.flag_ru
        ));
        list.add(new AboutAppTranslator(
                "Denio",
                "deniosens@yandex.ru",
                R.drawable.flag_ru
        ));
        list.add(new AboutAppTranslator(
                "Ken Berns",
                "ken.berns@yahoo.de",
                R.drawable.flag_de
        ));
        list.add(new AboutAppTranslator(
                "Milan Andrejić",
                "amikia@hotmail.com",
                R.drawable.flag_sr
        ));
        list.add(new AboutAppTranslator(
                "Miguel Torrijos",
                "migueltg352340@gmail.com",
                R.drawable.flag_es
        ));
        list.add(new AboutAppTranslator(
                "juliomartinezrodenas",
                "https://github.com/juliomartinezrodenas",
                R.drawable.flag_es
        ));
        list.add(new AboutAppTranslator(
                "Andrea Carulli",
                "rctandrew100@gmail.com",
                R.drawable.flag_it
        ));
        list.add(new AboutAppTranslator(
                "Jurre Tas",
                "jurretas@gmail.com",
                R.drawable.flag_nl
        ));
        list.add(new AboutAppTranslator(
                "Jörg Meinhardt",
                "jorime@web.de",
                R.drawable.flag_de
        ));
        list.add(new AboutAppTranslator(
                "Olivér Paróczai",
                "oliver.paroczai@gmail.com",
                R.drawable.flag_hu
        ));
        list.add(new AboutAppTranslator(
                "Fabio Raitz",
                "fabioraitz@outlook.com",
                R.drawable.flag_br
        ));
        list.add(new AboutAppTranslator(
                "Gregor",
                "glakner@gmail.com",
                R.drawable.flag_si
        ));
        list.add(new AboutAppTranslator(
                "Paróczai Olivér",
                "https://github.com/OliverParoczai",
                R.drawable.flag_hu
        ));
        list.add(new AboutAppTranslator(
                "sodqe muhammad",
                "sodqe.younes@gmail.com",
                R.drawable.flag_ar
        ));
        list.add(new AboutAppTranslator(
                "Thorsten Eckerlein",
                "thorsten.eckerlein@gmx.de",
                R.drawable.flag_de
        ));
        list.add(new AboutAppTranslator(
                "Jiří Král",
                "jirkakral978@gmail.com",
                R.drawable.flag_cs
        ));
        list.add(new AboutAppTranslator(
                "Kamil",
                "invisiblehype@gmail.com",
                R.drawable.flag_pl
        ));
        list.add(new AboutAppTranslator(
                "Μιχάλης Καζώνης",
                "istrios@gmail.com",
                R.drawable.flag_el
        ));
        list.add(new AboutAppTranslator(
                "이서경",
                "ng0972@naver.com",
                R.drawable.flag_ko
        ));
        list.add(new AboutAppTranslator(
                "rikupin1105",
                "https://github.com/rikupin1105",
                R.drawable.flag_ja
        ));
        return list;
    }
}
