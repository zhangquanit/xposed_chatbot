package com.vivo.push;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

/* compiled from: LocalAliasTagsManager */
final class i implements Runnable {
    final /* synthetic */ ArrayList a;
    final /* synthetic */ LocalAliasTagsManager b;

    i(LocalAliasTagsManager localAliasTagsManager, ArrayList arrayList) {
        this.b = localAliasTagsManager;
        this.a = arrayList;
    }

    public final void run() {
        HashSet hashSet = new HashSet();
        Iterator it = this.a.iterator();
        while (it.hasNext()) {
            hashSet.add((String) it.next());
        }
        if (this.b.mSubscribeAppTagManager.delTags(hashSet)) {
            p.a().b(LocalAliasTagsManager.DEFAULT_LOCAL_REQUEST_ID, (ArrayList<String>) this.a);
        }
    }
}
