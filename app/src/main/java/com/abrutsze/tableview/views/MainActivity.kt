package com.abrutsze.tableview.views

import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abrutsze.tableview.R
import com.abrutsze.tableview.adapters.NewsAdapter
import com.abrutsze.tableview.connections.ServerConnections
import com.abrutsze.tableview.connections.ServerConnections.*
import com.abrutsze.tableview.models.NewsResponse
import com.abrutsze.tableview.utils.CheckInternet
import com.abrutsze.tableview.utils.CheckInternet.INTERNET_AVAILABLE
import com.abrutsze.tableview.utils.Permissions
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), ServerConnections.ServerResponse, CheckInternet.InternetState {

    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var permissions: Permissions

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        layoutManager = LinearLayoutManager(this)
        rv.layoutManager = layoutManager

        newsAdapter = NewsAdapter()
        rv.adapter = newsAdapter

        permissions = Permissions(this)


        val checkInternet = CheckInternet(this)
        checkInternet.registerForInternetState(this)
        checkInternet.isInternetAvailable

        if (permissions.canWork()) {
            getServerConnectionsInstance().registerForServerResponse(this)
            getServerConnectionsInstance().getNews()
        } else {
            permissions.gimmePermission()
        }

    }

    override fun serverResponseStatus(responseCode: Int, responseMessage: Any?, errorBody: String?) {
        when (responseCode) {
            ERROR_SERVER_TIMEOUT, ERROR_NO_ROUTE_TO_HOST_EXCEPTION, ERROR_UNKNOWN -> {
                Toast.makeText(this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show()
                return
            }
        }
        if (responseMessage is NewsResponse) {
            runOnUiThread { newsAdapter.addNews(responseMessage.posts) }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, @NonNull permissions: Array<String>, @NonNull grantResults: IntArray) {

        when (requestCode) {
            10 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getServerConnectionsInstance().registerForServerResponse(this)
                    getServerConnectionsInstance().getNews()
                } else {
                    Toast.makeText(this, getString(R.string.no_permission, this.permissions.getPermissionLabel(permissions[0], this)), Toast.LENGTH_LONG).show()
                    finish()
                }
            }
        }
    }

    override fun internetStateChanged(state: Int) {
        when (state) {
            INTERNET_AVAILABLE -> {
                getServerConnectionsInstance().registerForServerResponse(this)
                getServerConnectionsInstance().getNews()
            }
            else -> {
            }
        }
    }

}
