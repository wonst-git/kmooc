package com.programmers.kmooc.repositories

import android.util.Log
import com.programmers.kmooc.models.Lecture
import com.programmers.kmooc.models.LectureList
import com.programmers.kmooc.models.LectureSimple
import com.programmers.kmooc.network.HttpClient
import com.programmers.kmooc.utils.DateUtil
import org.json.JSONArray
import org.json.JSONObject

class KmoocRepository {

    /**
     * 국가평생교육진흥원_K-MOOC_강좌정보API
     * https://www.data.go.kr/data/15042355/openapi.do
     */

    private val httpClient = HttpClient("http://apis.data.go.kr/B552881/kmooc")
    private val serviceKey =
        "HRKKc2Ky2rBjeA73eXaU51na2q7dCSfTyRKp5m97nP2DdbK2/jAEYaCRjP2vbu8EQBTynsNrdUCim1OhQ45apA=="

    fun list(completed: (LectureList?) -> Unit) {
        httpClient.getJson(
            "/courseList",
            mapOf("serviceKey" to serviceKey, "Mobile" to 1)
        ) { result ->
            result.onSuccess {
                Log.d("result[Success]", "$it")
                completed(parseLectureList(JSONObject(it)))
            }

            result.onFailure {
                it.printStackTrace()
                Log.d("result[Failure]", "${it.message}")
                completed(null)
            }
        }
    }

    fun next(next: String, completed: (LectureList?) -> Unit) {
        httpClient.getJson(next, emptyMap()) { result ->
            result.onSuccess {
                Log.d("result[Success]", "$it")
                completed(parseLectureList(JSONObject(it)))
            }

            result.onFailure {
                it.printStackTrace()
                Log.d("result[Failure]", "${it.message}")
                completed(null)
            }
        }
    }

    fun detail(courseId: String, completed: (Lecture?) -> Unit) {
        httpClient.getJson(
            "/courseDetail",
            mapOf("CourseId" to courseId, "serviceKey" to serviceKey)
        ) { result ->
            result.onSuccess {
                Log.d("result[Success]", "$it")

                completed(parseLecture(JSONObject(it)))
            }
            result.onFailure {
                it.printStackTrace()
                Log.d("result[Failure]", "${it.message}")

                completed(null)
            }
        }
    }

    private fun parseLectureList(jsonObject: JSONObject): LectureList {
        return if (jsonObject.isNull("pagination")) {
            LectureList(0, 0, "", "", listOf())
        } else {
            val pagination = jsonObject.getJSONObject("pagination")
            LectureList(
                if (pagination.isNull("count")) 0 else pagination.getInt("count"),
                if (pagination.isNull("num_pages")) 0 else pagination.getInt("num_pages"),
                if (pagination.isNull("previous")) "" else pagination.getString("previous"),
                if (pagination.isNull("next")) "" else pagination.getString("next"),
                parseLectureSimple(
                    if (jsonObject.isNull("results")) JSONArray() else jsonObject.getJSONArray("results")
                )
            )
        }
    }

    private fun parseLecture(lecture: JSONObject): Lecture = Lecture(
        if (lecture.isNull("id")) "" else lecture.getString("id"),
        if (lecture.isNull("number")) "" else lecture.getString("number"),
        if (lecture.isNull("name")) "" else lecture.getString("name"),
        if (lecture.isNull("classfy_name")) "" else lecture.getString("classfy_name"),
        if (lecture.isNull("middle_classfy_name")) "" else lecture.getString("middle_classfy_name"),
        if (lecture.isNull("media")) {
            ""
        } else {
            if (lecture.getJSONObject("media").isNull("image")) {
                ""
            } else {
                if (lecture.getJSONObject("media").getJSONObject("image").isNull("large")) {
                    ""
                } else {
                    lecture.getJSONObject("media").getJSONObject("image").getString("large")
                }
            }
        },
        if (lecture.isNull("short_description")) "" else lecture.getString("short_description"),
        if (lecture.isNull("org_name")) "" else lecture.getString("org_name"),
        if (lecture.isNull("start")) ""
        else DateUtil.convertDate(lecture.getString("start")),
        if (lecture.isNull("end")) ""
        else DateUtil.convertDate(lecture.getString("end")),
        if (lecture.isNull("teachers")) "" else lecture.getString("teachers"),
        if (lecture.isNull("overview")) "" else lecture.getString("overview"),
    )

    private fun parseLectureSimple(jsonArray: JSONArray): List<LectureSimple> {
        return mutableListOf<LectureSimple>().apply {
            for (i in 0 until jsonArray.length()) {
                val lecture = jsonArray.getJSONObject(i)
                add(
                    LectureSimple(
                        if (lecture.isNull("id")) "" else lecture.getString("id"),
                        if (lecture.isNull("name")) "" else lecture.getString("name"),
                        if (lecture.isNull("org_name")) "" else lecture.getString("org_name"),
                        if (lecture.isNull("start")) ""
                        else DateUtil.convertDate(lecture.getString("start")),
                        if (lecture.isNull("end")) ""
                        else DateUtil.convertDate(lecture.getString("end")),
                        if (lecture.isNull("media")) {
                            ""
                        } else {
                            if (lecture.getJSONObject("media").isNull("image")) {
                                ""
                            } else {
                                if (lecture.getJSONObject("media").getJSONObject("image").isNull("small")) {
                                    ""
                                } else {
                                    lecture.getJSONObject("media").getJSONObject("image").getString("small")
                                }
                            }
                        }
                    )
                )
            }
        }
    }
}