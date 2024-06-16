package com.example.mko_systems.service

import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.example.mko_systems.R
import com.example.mko_systems.data.UserRepository
import com.example.mko_systems.data.model.User
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class InstagramAccessibilityService : AccessibilityService() {

    @Inject
    lateinit var repository: UserRepository
    private val scope = CoroutineScope(Dispatchers.IO)

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        if (event.packageName == getString(R.string.com_instagram_android) &&
            event.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED
        ) {
            val rootNode = event.source ?: return
            val userName = getUserNameFromNode(rootNode)
            Log.d("InstagramService", "Found userName: $userName")
            if (!userName.isNullOrBlank()) {
                saveAccountName(userName)
            }
        }
    }

    private fun getUserNameFromNode(node: AccessibilityNodeInfo): String? {
        val textNodes = mutableListOf<String>()
        collectTextNodes(node, textNodes)
        val followingKeywords = listOf(
            getString(R.string.following_eng),
            getString(R.string.following_ru)
        )
        for (i in textNodes.indices) {
            if (followingKeywords.contains(textNodes[i].lowercase())) {
                if (i + 1 < textNodes.size) {
                    return textNodes[i + 1]
                }
            }
        }
        return null
    }

    private fun collectTextNodes(node: AccessibilityNodeInfo, textNodes: MutableList<String>) {
        if (
            node.className == getString(R.string.android_widget_textview) &&
            node.text != null
        ) {
            val text = node.text.toString()
            textNodes.add(text)
        }

        for (i in 0 until node.childCount) {
            val child = node.getChild(i)
            if (child != null) {
                collectTextNodes(child, textNodes)
                child.recycle()
            }
        }
    }

    private fun saveAccountName(username: String) {
        scope.launch(Dispatchers.IO) {
            repository.insertUser(User(name = username))
        }
    }

    override fun onInterrupt() {
        // No use
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }
}
