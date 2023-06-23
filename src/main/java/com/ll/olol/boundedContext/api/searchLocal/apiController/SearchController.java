package com.ll.olol.boundedContext.api.searchLocal.apiController;

import com.ll.olol.boundedContext.api.searchLocal.NaverSearchClient;
import com.ll.olol.boundedContext.api.searchLocal.SearchLocalReq;
import com.ll.olol.boundedContext.api.searchLocal.SearchLocalRes;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/server")
public class SearchController {
    private final NaverSearchClient naverSearchClient;

    public SearchController(NaverSearchClient naverSearchClient) {
        this.naverSearchClient = naverSearchClient;
    }

    @PostMapping("/search")
    public List<SearchLocalRes.Item> search(@RequestParam String query) {
        SearchLocalReq searchLocalReq = new SearchLocalReq();
        searchLocalReq.setQuery(query);

        SearchLocalRes searchLocalRes = naverSearchClient.searchLocal(searchLocalReq);

        List<SearchLocalRes.Item> items = new ArrayList<>();

        if (searchLocalRes != null && searchLocalRes.getItems() != null) {
            for (SearchLocalRes.Item item : searchLocalRes.getItems()) {
                String category = item.getCategory();
                if (category.equals("여행,명소>산,고개") || category.equals("지명>산"))
                    items.add(item);
            }
        }
        return items;
    }
}
