package com.bernardguiang.SnackOverflow.service;

import static java.time.temporal.TemporalAdjusters.firstDayOfYear;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.bernardguiang.SnackOverflow.dto.ProductDTO;
import com.bernardguiang.SnackOverflow.dto.UserDTO;
import com.bernardguiang.SnackOverflow.dto.request.StatsRequest;
import com.bernardguiang.SnackOverflow.dto.request.UserPage;
import com.bernardguiang.SnackOverflow.dto.response.FullUserDTO;
import com.bernardguiang.SnackOverflow.dto.response.OrderStatsResponse;
import com.bernardguiang.SnackOverflow.dto.response.UserStatsResponse;
import com.bernardguiang.SnackOverflow.model.Order;
import com.bernardguiang.SnackOverflow.model.Product;
import com.bernardguiang.SnackOverflow.model.User;
import com.bernardguiang.SnackOverflow.repository.UserRepository;

@Service
public class UserService {
	private final UserRepository userRepository;

	@Autowired
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	// Find all this month, year, all
	public UserStatsResponse getUserStats(StatsRequest request) {

		List<User> result = null;
		switch (request.getRange()) {
		case "all":
			Iterable<User> iterable = userRepository.findAll();
			result = new ArrayList<>();
			iterable.forEach(result::add);
			break;
		case "month":
			LocalDate firstDayOfMonth = LocalDate.now().withDayOfMonth(1);
			Instant monthStart = firstDayOfMonth.atStartOfDay(ZoneId.systemDefault()).toInstant();
			result = userRepository.findAllByJoinDateAfter(monthStart);
			break;
		case "year":
			LocalDate firstDayOfYear = LocalDate.now().with(firstDayOfYear());
			Instant yearStart = firstDayOfYear.atStartOfDay(ZoneId.systemDefault()).toInstant();
			result = userRepository.findAllByJoinDateAfter(yearStart);
			break;
		}
		
		return new UserStatsResponse(result.size());
	}

	// TODO: test
	public Page<UserDTO> findUsersPaginated(UserPage page) {
		Sort sort = Sort.by(page.getSortDirection(), page.getSortBy());
		Pageable pageable = PageRequest.of(page.getPageNumber(), page.getPageSize(), sort);

		Page<User> result = userRepository.findAllByUsernameContainingIgnoreCase(page.getSearch(), pageable);

		// Returns a new Page with the content of the current one mapped by the given
		// Function.
		Page<UserDTO> dtoPage = result.map(new Function<User, UserDTO>() {
			@Override
			public UserDTO apply(User entity) {
				UserDTO dto = new UserDTO(entity);
				return dto;
			}
		});

		return dtoPage;
	}

	// TODO: test
	public UserDTO findByUsername(String username) {
		Optional<User> user = userRepository.findByUsername(username);
		user.orElseThrow(() -> new IllegalStateException("Could not find user: " + username));
		UserDTO userDTO = new UserDTO(user.get());
		return userDTO;
	}

	// TODO: test.
	public FullUserDTO findById(Long userId) {
		Optional<User> user = userRepository.findById(userId);
		user.orElseThrow(() -> new IllegalStateException("Could not find user with id: " + userId));
		FullUserDTO userDTO = new FullUserDTO(user.get());
		return userDTO;
	}
}
