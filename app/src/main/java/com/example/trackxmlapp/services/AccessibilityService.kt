package com.example.trackxmlapp.services

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.accessibilityservice.GestureDescription
import android.graphics.Path
import android.os.Bundle
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.example.trackxmlapp.models.PageXml

class MyAccessibilityService : AccessibilityService() {
    private val listPageXml = mutableListOf<PageXml>()
    private var rootView: AccessibilityNodeInfo? = null
    private var lastEventData: String? = null

    override fun onServiceConnected() {
        val info = AccessibilityServiceInfo()

        info.apply {
            eventTypes =
                AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED or AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED
            feedbackType = AccessibilityServiceInfo.FEEDBACK_SPOKEN
            notificationTimeout = 100
        }

        this.serviceInfo = info
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        val allowedPackageNames =
            listOf("sinet.startup.inDriver", "com.google.android.youtube")

        when (event?.eventType) {
            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> {
                val currentPackageName = event.packageName.toString()
                if (allowedPackageNames.contains(currentPackageName)) {
                    rootView = event.source
                    if (rootView != null) {
                        val currentData = rootView.toString()

                        if (currentData == lastEventData) {
                            Log.d("AccessibilityService", "Event diabaikan karena data sama.")
                            return
                        }

                        logViewHierarchy(rootView)

                        lastEventData = currentData

                    } else {
                        Log.d("AccessibilityService", "Root node tidak ditemukan.")
                    }
                }
            }

//            AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED -> {
//                val currentPackageName = event.packageName.toString()
//                if (allowedPackageNames.contains(currentPackageName)) {
//                    rootView = event.source
//                    if (rootView != null) {
//                        val currentData = rootView.toString()
//
//                        if (currentData == lastEventData) {
//                            Log.d("AccessibilityService", "Event diabaikan karena data sama.")
//                            return
//                        }
//
//                        logViewHierarchy(rootView)
//
//                        lastEventData = currentData
//
//                        exploreViewHierarchy(rootView!!, "Window Updated") {
//                            logPageXmlAndSend()
//                        }
//                    } else {
//                        Log.d("AccessibilityService", "Root node tidak ditemukan.")
//                    }
//                }
//            }
        }
    }

    fun logViewHierarchy(node: AccessibilityNodeInfo?, depth: Int = 0) {
        if (node == null) return
        val prefix = " ".repeat(depth * 2)
        Log.d(
            "ViewHierarchy",
            "$prefix Class: ${node.className}, ContentDescription: ${node.contentDescription}, Text: ${node.text}"
        )

        if (node.isScrollable) {
            performPullToRefreshGesture()
            val scrolled = node.performAction(AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD)
            Log.d(
                "ViewHierarchy",
                "$prefix Scrollable node ditemukan. Swipe: $scrolled"
            )
        }
        for (i in 0 until node.childCount) {
            logViewHierarchy(node.getChild(i), depth + 1)
        }
    }

    private fun performPullToRefreshGesture() {
        val path = Path().apply {
            moveTo(500f, 500f)
            lineTo(500f, 1500f)
        }

        val gesture = GestureDescription.Builder().apply {
            addStroke(GestureDescription.StrokeDescription(path, 0, 2000L))
        }.build()

        dispatchGesture(
            gesture,
            object : GestureResultCallback() {
                override fun onCompleted(gestureDescription: GestureDescription?) {
                    Log.d("Gesture", "Pull-to-refresh gesture completed.")
                }

                override fun onCancelled(gestureDescription: GestureDescription?) {
                    Log.d("Gesture", "Pull-to-refresh gesture cancelled.")
                }
            },
            null
        )
    }

    override fun onInterrupt() {
        Log.d("AccessibilityService", "Layanan Aksesibilitas terhenti atau dihentikan")
    }

    private fun exploreViewHierarchy(
        view: AccessibilityNodeInfo,
        type: String,
        onCompleted: () -> Unit
    ) {
        if (view.childCount > 0) {
            for (i in 0 until view.childCount) {
                val child = view.getChild(i)
                if (child != null) {
                    exploreViewHierarchy(child, type, onCompleted)
                }
            }
        }

        val id = view.toString().substringBefore(";")
        var result = id.substringAfterLast(".")

        val pageXml = PageXml(
            type = type,
            id = result,
            content = view.toString()
        )

        listPageXml.add(pageXml)

        if (rootView == view)
            logPageXmlAndSend()
    }

    fun clickElementWithContentDescription(
        rootNode: AccessibilityNodeInfo?,
        targetDescription: String
    ) {
        if (rootNode == null) return

        for (i in 0 until rootNode.childCount) {
            val childNode = rootNode.getChild(i)
            if (childNode != null) {

                if (childNode.contentDescription == targetDescription && childNode.className == "android.widget.ImageView") {
                    val isClickable = childNode.isClickable
                    if (isClickable) {
                        val success = childNode.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                        if (success) {
                            Log.d(
                                "AccessibilityService",
                                "Berhasil mengklik elemen dengan contentDescription: $targetDescription"
                            )
                            return
                        } else {
                            Log.d(
                                "AccessibilityService",
                                "Gagal mengklik elemen dengan contentDescription: $targetDescription"
                            )
                        }
                    } else {
                        Log.d(
                            "AccessibilityService",
                            "Elemen dengan contentDescription: $targetDescription tidak dapat diklik."
                        )
                    }
                }
                clickElementWithContentDescription(childNode, targetDescription)
            }
        }
    }

    fun fillEditTextWithSearchQuery(rootNode: AccessibilityNodeInfo?) {
        if (rootNode == null) return

        for (i in 0 until rootNode.childCount) {
            val childNode = rootNode.getChild(i)
            if (childNode != null) {

                if (childNode.className == "android.widget.EditText" && childNode.text == "Search YouTube") {
                    val bundle = Bundle()
//                    For Example
                    bundle.putCharSequence(
                        AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE,
                        "apt india"
                    )

                    val actionResult =
                        childNode.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, bundle)
                    if (actionResult) {
                        Log.d("AccessibilityService", "Berhasil mengisi EditText dengan teks 'apt'")
                    } else {
                        Log.d("AccessibilityService", "Gagal mengisi EditText dengan teks 'apt'")
                    }
                    return
                }

                fillEditTextWithSearchQuery(childNode)
            }
        }
    }

    fun pressVideoButton(rootNode: AccessibilityNodeInfo?) {
        if (rootNode == null) return

        for (i in 0 until rootNode.childCount) {
            val childNode = rootNode.getChild(i)
            if (childNode != null) {
                if (childNode.className == "android.view.ViewGroup" || childNode.className == "android.widget.Button") {
                    val contentDescription = childNode.contentDescription?.toString()
                    val text = childNode.text?.toString()

//                    For Example
                    if (contentDescription?.contains(
                            "Indonesian Koplo | DJ Vleezy Bootleg",
                            ignoreCase = true
                        ) == true || text?.contains(
                            "Indonesian Koplo | DJ Vleezy Bootleg",
                            ignoreCase = true
                        ) == true
                    ) {
                        if (childNode.isClickable) {
                            val success =
                                childNode.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                            if (success) {
                                Log.d("AccessibilityService", "Berhasil mengklik tombol video")
                            } else {
                                Log.d("AccessibilityService", "Gagal mengklik tombol video")
                            }
                        } else {
                            Log.d("AccessibilityService", "Tombol video tidak dapat diklik.")
                        }
                        return
                    }
                }

                pressVideoButton(childNode)
            }
        }
    }

    private fun logPageXmlAndSend() {
        logPageXml()
//        val requestData = RequestData(pageXml = listPageXml)
//        val repository = Repository()
//        coroutineScope.launch {
//            try {
//                repository.sendDataToServer(requestData)
//                withContext(Dispatchers.Main) {
//                    Log.d("AccessibilityService", "Data berhasil dikirim.")
//                    listPageXml.clear()
//                }
//            } catch (e: Exception) {
//                withContext(Dispatchers.Main) {
//                    Log.e("AccessibilityService", "Error: ${e.message}")
//                }
//            }
//        }
    }

    private fun logPageXml() {
        for (page in listPageXml) {
            Log.d(
                "PageXml",
                "type: ${page.type}, id: ${page.id}, content: ${page.content}"
            )
        }
        Log.d(
            "PageXml",
            "=================================="
        )
        listPageXml.clear()
    }
}