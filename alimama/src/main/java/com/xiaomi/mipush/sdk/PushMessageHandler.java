package com.xiaomi.mipush.sdk;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.IBinder;
import android.text.TextUtils;
import com.xiaomi.channel.commonutils.logger.b;
import com.xiaomi.mipush.sdk.MessageHandleService;
import com.xiaomi.mipush.sdk.MiPushClient;
import com.xiaomi.push.ev;
import com.xiaomi.push.fa;
import com.xiaomi.push.hk;
import com.xiaomi.push.iq;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class PushMessageHandler extends BaseService {
    private static List<MiPushClient.MiPushClientCallback> a = new ArrayList();

    /* renamed from: a  reason: collision with other field name */
    private static ThreadPoolExecutor f27a = new ThreadPoolExecutor(1, 1, 15, TimeUnit.SECONDS, new LinkedBlockingQueue());

    interface a extends Serializable {
    }

    protected static void a() {
        synchronized (a) {
            a.clear();
        }
    }

    public static void a(long j, String str, String str2) {
        synchronized (a) {
            for (MiPushClient.MiPushClientCallback onInitializeResult : a) {
                onInitializeResult.onInitializeResult(j, str, str2);
            }
        }
    }

    public static void a(Context context) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(context, PushMessageHandler.class));
        try {
            context.startService(intent);
        } catch (Exception e) {
            b.a(e.getMessage());
        }
    }

    public static void a(Context context, Intent intent) {
        b.c("addjob PushMessageHandler " + intent);
        if (intent != null) {
            c(context, intent);
            a(context);
        }
    }

    private static void a(Context context, Intent intent, ResolveInfo resolveInfo) {
        try {
            MessageHandleService.addJob(context.getApplicationContext(), new MessageHandleService.a(intent, (PushMessageReceiver) Class.forName(resolveInfo.activityInfo.name).newInstance()));
            MessageHandleService.a(context, new Intent(context.getApplicationContext(), MessageHandleService.class));
        } catch (Throwable th) {
            b.a(th);
        }
    }

    public static void a(Context context, MiPushMessage miPushMessage) {
        synchronized (a) {
            for (MiPushClient.MiPushClientCallback next : a) {
                if (a(miPushMessage.getCategory(), next.getCategory())) {
                    next.onReceiveMessage(miPushMessage.getContent(), miPushMessage.getAlias(), miPushMessage.getTopic(), miPushMessage.isNotified());
                    next.onReceiveMessage(miPushMessage);
                }
            }
        }
    }

    public static void a(Context context, a aVar) {
        if (aVar instanceof MiPushMessage) {
            a(context, (MiPushMessage) aVar);
        } else if (aVar instanceof MiPushCommandMessage) {
            MiPushCommandMessage miPushCommandMessage = (MiPushCommandMessage) aVar;
            String command = miPushCommandMessage.getCommand();
            String str = null;
            if (fa.COMMAND_REGISTER.f328a.equals(command)) {
                List<String> commandArguments = miPushCommandMessage.getCommandArguments();
                if (commandArguments != null && !commandArguments.isEmpty()) {
                    str = commandArguments.get(0);
                }
                a(miPushCommandMessage.getResultCode(), miPushCommandMessage.getReason(), str);
            } else if (fa.COMMAND_SET_ALIAS.f328a.equals(command) || fa.COMMAND_UNSET_ALIAS.f328a.equals(command) || fa.COMMAND_SET_ACCEPT_TIME.f328a.equals(command)) {
                a(context, miPushCommandMessage.getCategory(), command, miPushCommandMessage.getResultCode(), miPushCommandMessage.getReason(), miPushCommandMessage.getCommandArguments());
            } else if (fa.COMMAND_SUBSCRIBE_TOPIC.f328a.equals(command)) {
                List<String> commandArguments2 = miPushCommandMessage.getCommandArguments();
                if (commandArguments2 != null && !commandArguments2.isEmpty()) {
                    str = commandArguments2.get(0);
                }
                Context context2 = context;
                a(context2, miPushCommandMessage.getCategory(), miPushCommandMessage.getResultCode(), miPushCommandMessage.getReason(), str);
            } else if (fa.COMMAND_UNSUBSCRIBE_TOPIC.f328a.equals(command)) {
                List<String> commandArguments3 = miPushCommandMessage.getCommandArguments();
                if (commandArguments3 != null && !commandArguments3.isEmpty()) {
                    str = commandArguments3.get(0);
                }
                Context context3 = context;
                b(context3, miPushCommandMessage.getCategory(), miPushCommandMessage.getResultCode(), miPushCommandMessage.getReason(), str);
            }
        }
    }

    protected static void a(Context context, String str, long j, String str2, String str3) {
        synchronized (a) {
            for (MiPushClient.MiPushClientCallback next : a) {
                if (a(str, next.getCategory())) {
                    next.onSubscribeResult(j, str2, str3);
                }
            }
        }
    }

    protected static void a(Context context, String str, String str2, long j, String str3, List<String> list) {
        synchronized (a) {
            for (MiPushClient.MiPushClientCallback next : a) {
                if (a(str, next.getCategory())) {
                    next.onCommandResult(str2, j, str3, list);
                }
            }
        }
    }

    protected static void a(MiPushClient.MiPushClientCallback miPushClientCallback) {
        synchronized (a) {
            if (!a.contains(miPushClientCallback)) {
                a.add(miPushClientCallback);
            }
        }
    }

    protected static boolean a(String str, String str2) {
        return (TextUtils.isEmpty(str) && TextUtils.isEmpty(str2)) || TextUtils.equals(str, str2);
    }

    protected static void b(Context context, Intent intent) {
        try {
            ResolveInfo resolveInfo = null;
            if ("com.xiaomi.mipush.sdk.WAKEUP".equals(intent.getAction())) {
                p.a(context, intent, (Uri) null);
            } else if ("com.xiaomi.mipush.SEND_TINYDATA".equals(intent.getAction())) {
                hk hkVar = new hk();
                iq.a(hkVar, intent.getByteArrayExtra("mipush_payload"));
                b.c("PushMessageHandler.onHandleIntent " + hkVar.d());
                MiTinyDataClient.upload(context, hkVar);
            } else if (1 == PushMessageHelper.getPushMode(context)) {
                if (b()) {
                    b.d("receive a message before application calling initialize");
                    return;
                }
                a a2 = av.a(context).a(intent);
                if (a2 != null) {
                    a(context, a2);
                }
            } else if ("com.xiaomi.mipush.sdk.SYNC_LOG".equals(intent.getAction())) {
                Logger.uploadLogFile(context, false);
            } else {
                Intent intent2 = new Intent("com.xiaomi.mipush.RECEIVE_MESSAGE");
                intent2.setPackage(context.getPackageName());
                intent2.putExtras(intent);
                List<ResolveInfo> queryBroadcastReceivers = context.getPackageManager().queryBroadcastReceivers(intent2, 32);
                if (queryBroadcastReceivers != null) {
                    Iterator<ResolveInfo> it = queryBroadcastReceivers.iterator();
                    while (true) {
                        if (!it.hasNext()) {
                            break;
                        }
                        ResolveInfo next = it.next();
                        if (next.activityInfo != null && next.activityInfo.packageName.equals(context.getPackageName()) && PushMessageReceiver.class.isAssignableFrom(Class.forName(next.activityInfo.name))) {
                            resolveInfo = next;
                            break;
                        }
                    }
                }
                if (resolveInfo != null) {
                    a(context, intent2, resolveInfo);
                    return;
                }
                b.d("cannot find the receiver to handler this message, check your manifest");
                ev.a(context).a(context.getPackageName(), intent, "cannot find the receiver to handler this message, check your manifest");
            }
        } catch (Exception e) {
            b.a((Throwable) e);
            ev.a(context).a(context.getPackageName(), intent, (Throwable) e);
        } catch (Throwable th) {
            b.a(th);
            ev.a(context).a(context.getPackageName(), intent, th);
        }
    }

    protected static void b(Context context, String str, long j, String str2, String str3) {
        synchronized (a) {
            for (MiPushClient.MiPushClientCallback next : a) {
                if (a(str, next.getCategory())) {
                    next.onUnsubscribeResult(j, str2, str3);
                }
            }
        }
    }

    public static boolean b() {
        return a.isEmpty();
    }

    private static void c(Context context, Intent intent) {
        if (intent != null && !f27a.isShutdown()) {
            f27a.execute(new au(context, intent));
        }
    }

    /* access modifiers changed from: protected */
    /* renamed from: a  reason: collision with other method in class */
    public boolean m31a() {
        return (f27a == null || f27a.getQueue() == null || f27a.getQueue().size() <= 0) ? false : true;
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onStart(Intent intent, int i) {
        super.onStart(intent, i);
        c(getApplicationContext(), intent);
    }
}
