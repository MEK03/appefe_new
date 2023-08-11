package com.example.testing

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.shopify.buy3.GraphCallResult
import com.shopify.buy3.GraphCallResultCallback
import com.shopify.buy3.GraphClient
import com.shopify.buy3.GraphError
import com.shopify.buy3.GraphResponse
import com.shopify.buy3.QueryGraphCall
import com.shopify.buy3.Storefront
import com.shopify.buy3.Storefront.QueryRootQuery
import com.shopify.buy3.Storefront.ShopQuery
import com.shopify.buy3.Storefront.query

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        executeGraphQuery()
    }

    fun goToCategoryPage() {
        val intent = Intent(this, MainActivity2::class.java)
        startActivity(intent)
    }

    private fun executeGraphQuery() {
        val client: GraphClient = GraphClient.build(
            this,
            "xennio.myshopify.com",
            "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"
        )

        val query = query { rootQuery: QueryRootQuery ->
            rootQuery
                .shop { shopQuery: ShopQuery ->
                    shopQuery
                        .name()
                }
        }

        class MyGraphCallback : GraphCallResultCallback<Storefront.QueryRoot> {
            fun onResponse(response: GraphResponse<Storefront.QueryRoot>) {
                val name = response.data?.shop?.name
                // Your onResponse logic here
            }


            fun onFailure(error: GraphError) {
                Log.e("InUse", "Failed to execute query", error)
                // Your onFailure logic here
            }

            override operator fun invoke(result: GraphCallResult<Storefront.QueryRoot>) {
                when (result) {
                    is GraphCallResult.Success -> onResponse(result.response)
                    is GraphCallResult.Failure -> onFailure(result.error)
                }
            }
        }

        val call: QueryGraphCall = client.queryGraph(query)
        val callback = MyGraphCallback()
        call.enqueue(callback)


    }
}
